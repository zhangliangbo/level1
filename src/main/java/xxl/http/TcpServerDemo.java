package xxl.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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
        .doOnBind(new Consumer<ServerBootstrap>() {
          @Override
          public void accept(ServerBootstrap serverBootstrap) {
            System.err.println("bind");
          }
        })
        .doOnBound(new Consumer<DisposableServer>() {
          @Override
          public void accept(DisposableServer disposableServer) {
            System.err.println("bound: " + disposableServer.address().getHostName() + ":" + disposableServer.address().getPort());
          }
        })
        .doOnUnbound(new Consumer<DisposableServer>() {
          @Override
          public void accept(DisposableServer disposableServer) {
            System.err.println("unbound: " + disposableServer.address().getHostName() + ":" + disposableServer.address().getPort());
          }
        })
        .doOnConnection(new Consumer<Connection>() {
          @Override
          public void accept(Connection connection) {
            connection.addHandler(new ChannelHandler() {
              @Override
              public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

              }

              @Override
              public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

              }

              @Override
              public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

              }
            });
          }
        })
        .handle(new Function2<NettyInbound, NettyOutbound, Publisher<Void>>() {
          @Override
          public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
            return null;
          }
        })
        .host("localhost")
        .port(port)
        .bindNow();
    server.onDispose().block();
  }
}
