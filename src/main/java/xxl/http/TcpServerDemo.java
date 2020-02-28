package xxl.http;

import io.netty.buffer.ByteBuf;
import io.vavr.Function2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.reactivestreams.Publisher;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.tcp.TcpServer;

import java.util.function.Consumer;

public class TcpServerDemo {
  public static void main(String[] args) {
    String portOpt = "port";
    Options options = new Options()
        .addOption(portOpt, true, "端口");
    CommandLine cli;
    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.err.println(options);
      return;
    }
    int port = Integer.parseInt(cli.getOptionValue(portOpt, "8080"));
    DisposableServer server = TcpServer.create()
        .doOnConnection(new Consumer<Connection>() {
          @Override
          public void accept(Connection connection) {
            System.err.println("accept connection: " + connection.address().getHostName() + ":" + connection.address().getPort());
          }
        })
        .handle(new Function2<NettyInbound, NettyOutbound, Publisher<Void>>() {
          @Override
          public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
            return nettyOutbound.send(nettyInbound.receive().doOnNext(new Consumer<ByteBuf>() {
              @Override
              public void accept(ByteBuf byteBuf) {
                System.err.println(new String(byteBuf.array()));
              }
            })).then();
          }
        })
        .host("localhost")
        .port(port)
        .bindNow();
    server.onDispose().block();
  }
}
