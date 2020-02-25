package xxl.rabbitmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Scanner;

public class ExchangeDemo {
  public static void main(String[] args) {
    String exOpt = "exchange";
    String exType = "type";
    String exRo = "routing";
    Options options = new Options()
        .addOption(exOpt, true, "交换器")
        .addOption(exType, true, "交换机类型")
        .addOption(exRo, true, "路由键");
    CommandLine cli;
    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.err.println(options);
      return;
    }
    String ex = cli.getOptionValue(exOpt, "");
    String ro = cli.getOptionValue(exRo, "");
    String type = cli.getOptionValue(exType, "fanout");
    RabbitMQ rabbitMQ = new RabbitMQ(
        new String[]{"localhost:5672"},
        "mqtt",
        "mqtt",
        "/",
        true
    );
    if (rabbitMQ.newChannel()) {
      if (rabbitMQ.confirmSelect()) {//打开消息确认模式
        if (rabbitMQ.exchangeDeclare(ex, type, true, false)) {
          Scanner sc = new Scanner(System.in);
          while (sc.hasNextLine()) {
            String[] items = sc.nextLine().split(" +");
            int count;
            String content;
            if (items.length > 1) {
              content = items[0];
              count = Integer.parseInt(items[1]);
            } else {
              if ("quit".equals(items[0])) {
                break;
              } else {
                content = items[0];
                count = 1;
              }
            }
            for (int i = 0; i < count; i++) {
              if (rabbitMQ.publish(ex, ro, (content + i).getBytes())) {
                System.err.println("send " + content + " " + i);
                if (rabbitMQ.waitForConfirms()) {
                  System.err.println("confirmed");
                } else {
                  System.err.println("not confirmed");
                }
              }
            }
          }
        }
      }
    }
    rabbitMQ.close();
    System.err.println("finished");
  }
}
