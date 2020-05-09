package xxl.reactornetty;

import io.vavr.Function2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.text.StringEscapeUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.tcp.TcpClient;

import java.util.Scanner;
import java.util.function.Consumer;

public class TcpClientDemo {
    public static void main(String[] args) {
        String portOpt = "port";
        String hostOpt = "host";
        String suffixOpt = "suffix";
        Options options = new Options()
                .addOption(hostOpt, true, "地址")
                .addOption(portOpt, true, "端口")
                .addOption(suffixOpt, true, "消息分隔后缀");
        CommandLine cli;
        try {
            cli = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            System.err.println(options);
            return;
        }
        if (cli.hasOption("help")) {
            System.err.println(options);
            return;
        }
        String host = cli.getOptionValue(hostOpt, "localhost");
        int port = Integer.parseInt(cli.getOptionValue(portOpt, "8080"));
        String suffix = StringEscapeUtils.unescapeJson(cli.getOptionValue(suffixOpt, ""));
        Connection connection = TcpClient.create()
                .doOnConnected(new Consumer<Connection>() {
                    @Override
                    public void accept(Connection connection) {
                        System.err.println("connected");
                    }
                })
                .handle(new Function2<NettyInbound, NettyOutbound, Publisher<Void>>() {
                    @Override
                    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
                        return Flux.merge(
                                nettyInbound.receive().asString().doOnNext(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) {
                                        System.out.println("client receive: " + s.replace(suffix, ""));
                                    }
                                }).subscribeOn(Schedulers.newSingle("ClientRead")),
                                nettyOutbound.sendString(Flux.create(new Consumer<FluxSink<String>>() {
                                    @Override
                                    public void accept(FluxSink<String> stringFluxSink) {
                                        Scanner sc = new Scanner(System.in);
                                        while (true) {
                                            System.err.println("请输入发送内容或quit退出");
                                            String line = sc.nextLine();
                                            if ("quit".equals(line)) {
                                                break;
                                            } else {
                                                stringFluxSink.next(line + suffix);
                                            }
                                        }
                                        stringFluxSink.complete();
                                    }
                                }).subscribeOn(Schedulers.newSingle("ClientWrite")))).then();
                    }
                })
                .host(host)
                .port(port)
                .connectNow();
        connection.onDispose().block();
    }
}
