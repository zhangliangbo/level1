package xxl.http;

import io.netty.bootstrap.Bootstrap;
import io.vavr.Function2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.reactivestreams.Publisher;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.tcp.TcpClient;

import java.util.function.Consumer;

public class TcpClientDemo {
  public static void main(String[] args) {
    String portOpt = "port";
    String hostOpt = "host";
    Options options = new Options()
        .addOption(hostOpt, true, "地址")
        .addOption(portOpt, true, "端口");
    CommandLine cli;
    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.err.println(options);
      return;
    }
    String host = cli.getOptionValue(hostOpt, "localhost");
    int port = Integer.parseInt(cli.getOptionValue(portOpt, "8080"));
    Connection connection = TcpClient.create()
        .doOnConnect(new Consumer<Bootstrap>() {
          @Override
          public void accept(Bootstrap bootstrap) {
            System.err.println("connect");
          }
        })
        .doOnConnected(new Consumer<Connection>() {
          @Override
          public void accept(Connection connection) {
            System.err.println("connected: " + connection.address().getHostName() + ":" + connection.address().getPort());
          }
        })
        .doOnDisconnected(new Consumer<Connection>() {
          @Override
          public void accept(Connection connection) {
            System.err.println("disconnected: " + connection.address().getHostName() + ":" + connection.address().getPort());
          }
        })
        .handle(new Function2<NettyInbound, NettyOutbound, Publisher<Void>>() {
          @Override
          public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
            return nettyInbound.receive().then();
          }
        })
        .host(host)
        .port(port)
        .connectNow();
    connection.onDispose().block();
  }
}
