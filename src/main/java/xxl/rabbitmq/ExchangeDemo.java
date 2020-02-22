package xxl.rabbitmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ExchangeDemo {
  public static void main(String[] args) {
    String exOpt = "exchange";
    String exType = "type";
    String exRo = "routing";
    Options options = new Options()
        .addOption(exOpt, true, "交换器")
        .addOption(exRo, true, "路由键");
    CommandLine cli;
    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.err.println(options);
      return;
    }
    String ex = cli.getOptionValue(exOpt, "exchange");
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
          for (int i = 0; i < 1000; i++) {
            if (rabbitMQ.publish(ex, ro, ("hello" + i).getBytes())) {
              System.err.println("send hello " + i);
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
    rabbitMQ.close();
    System.err.println("finished");
  }
}
