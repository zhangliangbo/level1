package xxl.reactornetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.vavr.Function2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.text.StringEscapeUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.tcp.TcpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public class TcpServerDemo {
    public static void main(String[] args) {
        String portOpt = "port";
        String suffixOpt = "suffix";
        String helloOpt = "hello";
        String noReplyOpt = "noReply";
        Options options = new Options()
                .addOption(portOpt, true, "端口")
                .addOption(suffixOpt, true, "消息分割符")
                .addOption(helloOpt, "主动打招呼")
                .addOption(noReplyOpt, "是否不应答");

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
        int port = Integer.parseInt(cli.getOptionValue(portOpt, "8080"));
        String suffix = StringEscapeUtils.unescapeJson(cli.getOptionValue(suffixOpt, ""));
        boolean hello = cli.hasOption(helloOpt);
        boolean noReply = cli.hasOption(noReplyOpt);
        ChannelGroup cg = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        DisposableServer server = TcpServer.create()
                .doOnBind(new Consumer<ServerBootstrap>() {
                    @Override
                    public void accept(ServerBootstrap serverBootstrap) {
                        System.err.println("bind");
                    }
                })
                .doOnConnection(new Consumer<Connection>() {
                    @Override
                    public void accept(Connection connection) {
                        connection.addHandler(new ChannelHandler() {
                            @Override
                            public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                                InetSocketAddress isa = (InetSocketAddress) ctx.channel().remoteAddress();
                                System.err.println(isa.getHostName() + ":" + isa.getPort() + " added");
                                cg.add(ctx.channel());
                                System.err.println("group has " + cg.size());
                            }

                            @Override
                            public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                                InetSocketAddress isa = (InetSocketAddress) ctx.channel().remoteAddress();
                                System.err.println(isa.getHostName() + ":" + isa.getPort() + " removed");
                                cg.remove(ctx.channel());
                                System.err.println("group has " + cg.size());
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                InetSocketAddress isa = (InetSocketAddress) ctx.channel().remoteAddress();
                                System.err.println(isa.getHostName() + ":" + isa.getPort() + " caught");
                                cg.remove(ctx.channel());
                                System.err.println("group has " + cg.size());
                            }
                        });
                    }
                })
                .handle(new Function2<NettyInbound, NettyOutbound, Publisher<Void>>() {
                    @Override
                    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
                        AtomicReference<Connection> conn = new AtomicReference<>();
                        return Flux.concat(
                                hello ? nettyOutbound.sendString(Mono.just("hello" + suffix)) : nettyOutbound.then(),
                                nettyInbound.withConnection(new Consumer<Connection>() {
                                    @Override
                                    public void accept(Connection connection) {
                                        connection.channel();
                                        conn.set(connection);
                                    }
                                }).receive().asString().flatMap(new Function<String, Publisher<? extends Void>>() {
                                    @Override
                                    public Publisher<? extends Void> apply(String s) {
                                        System.err.println(conn.get().address().getHostName() + ":" + conn.get().address().getPort() + "\n" + s.replace(suffix, ""));
                                        if (noReply) {
                                            return nettyOutbound.then();
                                        } else {
                                            return nettyOutbound.sendString(Mono.just(s + " travel from server" + suffix));
                                        }
                                    }
                                }));
                    }
                })
                .host("localhost")
                .port(port)
                .bindNow();
        server.onDispose().block();
    }
}
