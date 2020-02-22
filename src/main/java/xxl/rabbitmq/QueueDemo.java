package xxl.rabbitmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class QueueDemo {
  public static void main(String[] args) {
    String exOpt = "exchange";
    String quOpt = "queue";
    String roOpt = "routing";
    Options options = new Options()
        .addOption(exOpt, true, "交换机名称")
        .addOption(quOpt, true, "队列名称");
    CommandLine cli;
    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.err.println(options);
      return;
    }
    String ex = cli.getOptionValue(exOpt, "exchange");
    String qu = cli.getOptionValue(quOpt, "queue");
    String ro = cli.getOptionValue(roOpt, "");
    RabbitMQ rabbitMQ = new RabbitMQ(
        new String[]{"localhost:5672"},
        "mqtt",
        "mqtt",
        "/",
        true
    );
    if (rabbitMQ.newChannel()) {
      if (rabbitMQ.exchangeDeclare(ex)
          && rabbitMQ.queueDeclare(qu, true, false, false)
          && rabbitMQ.queueBind(qu, ex, ro)
      ) {
        if (rabbitMQ.qos(1, false)) {
          rabbitMQ.consume(qu, "random", new RecordConsumer() {
            @Override
            public void onDelivery(Record record) {
              if (record != null) {
                System.err.println(record);
                rabbitMQ.ack(record.deliveryTag(), false);
              }
            }
          });
//          while (true) {
//            Record record = rabbitMQ.get("queue", false);
//            if (record != null) {
//              System.err.println(record);
//              if (rabbitMQ.ack(record.deliveryTag(), false)) {
//                System.err.println("acknowledgement");
//              }
//            }
//          }
        }
      }
    }
  }
}
