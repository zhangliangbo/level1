package xxl.http;

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

import java.util.Scanner;
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
        .doOnConnected(new Consumer<Connection>() {
          @Override
          public void accept(Connection connection) {
            System.err.println("connected");
          }
        })
        .handle(new Function2<NettyInbound, NettyOutbound, Publisher<Void>>() {
          @Override
          public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
            System.err.println("client receive something");
            return nettyInbound.receive().asByteArray()
                .doOnNext(new Consumer<byte[]>() {
                  @Override
                  public void accept(byte[] bytes) {
                    System.err.println("receive: " + new String(bytes));
                  }
                }).then();
          }
        })
        .host(host)
        .port(port)
        .connectNow();
    Scanner sc = new Scanner(System.in);
    while (true) {
      System.err.println("请输入发送数据或输入quit退出");
      String line = sc.nextLine();
      if ("quit".equals(line)) {
        connection.dispose();
        break;
      } else {
        connection.channel().writeAndFlush(line.getBytes());
      }
    }
  }
}
