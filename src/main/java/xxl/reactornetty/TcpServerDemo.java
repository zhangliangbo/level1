package xxl.reactornetty;

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
import org.reactivestreams.Publisher;
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
    ChannelGroup cg = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    DisposableServer server = TcpServer.create()
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
            return nettyInbound.withConnection(new Consumer<Connection>() {
              @Override
              public void accept(Connection connection) {
                connection.channel();
                conn.set(connection);
              }
            }).receive().asString().flatMap(new Function<String, Publisher<? extends Void>>() {
              @Override
              public Publisher<? extends Void> apply(String s) {
                System.err.println(conn.get().address().getHostName() + ":" + conn.get().address().getPort() + "=" + s);
                return nettyOutbound.sendString(Mono.just(s));
              }
            });
          }
        })
        .host("localhost")
        .port(port)
        .bindNow();
    server.onDispose().block();
  }
}
