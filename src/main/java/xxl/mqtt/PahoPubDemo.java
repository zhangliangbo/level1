package xxl.mqtt;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Scanner;
import java.util.UUID;

public class PahoPubDemo {
  public static void main(String[] args) {
    String idOpt = "id";
    String topicOpt = "topic";
    String typeOpt = "type";
    Options options = new Options()
        .addOption(idOpt, true, "客户端id")
        .addOption(topicOpt, true, "发布的主题")
        .addOption(typeOpt, true, "发布者 订阅者");
    CommandLine cli;
    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.err.println(options);
      return;
    }
    String id = cli.getOptionValue(idOpt, UUID.randomUUID().toString());
    String topic = cli.getOptionValue(topicOpt, "publish");
    String type = cli.getOptionValue(topicOpt, "sub");
    Paho paho = new Paho("tcp://localhost:1883", id, true);
    paho.open();
    paho.setManualAck(true);
    if ("sub".equals(type)) {
      if (paho.subscribe(topic, 1, new PahoConsumer() {
        @Override
        public void onMessage(PahoMessage message) {
          System.err.println(message);
          paho.ack(message.id(), 1);
        }
      })) {
        System.err.println("subscribe succeed");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
          String line = sc.nextLine();
          if ("quit".equals(line)) {
            break;
          }
        }
      } else {
        System.err.println("subscribe failed");
      }
    } else {
      Scanner sc = new Scanner(System.in);
      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        if ("quit".equals(line)) {
          break;
        }
        int msgId = paho.publish(topic, line.getBytes(), 1, true);
        System.err.println("publish " + msgId);
      }
    }
    paho.disconnect();
    paho.close();
  }
}
