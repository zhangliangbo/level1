package xxl.mqtt;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Scanner;
import java.util.UUID;

public class PahoDemo {
    public static void main(String[] args) {
        String urlOpt = "url";
        String idOpt = "id";
        String topicOpt = "topic";
        String typeOpt = "type";
        Options options = new Options()
                .addOption(urlOpt, true, "服务器地址")
                .addOption(idOpt, true, "客户端id")
                .addOption(topicOpt, true, "发布的主题")
                .addOption(typeOpt, true, "发布者 订阅者");
        if (args.length < 2) {
            System.err.println(options);
            return;
        }
        CommandLine cli;
        try {
            cli = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            System.err.println(options);
            return;
        }
        String url = cli.getOptionValue(urlOpt, "tcp://localhost:1883");
        String id = cli.getOptionValue(idOpt, UUID.randomUUID().toString());
        String topic = cli.getOptionValue(topicOpt, "publish");
        String type = cli.getOptionValue(typeOpt, "sub");
        Paho paho = new Paho(url, id, true);
        paho.open();
        if (!paho.connect(new PahoOptions(
                "mqtt",
                "mqtt",
                true,
                false,
                new PahoMessage(topic, (id + " die").getBytes(), 1, 0, true, false)))) {
            System.err.println("连接失败");
            paho.close();
            return;
        }
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
                String[] items = line.split(" ");
                if (items.length > 1) {
                    String body = items[0];
                    int num = Integer.parseInt(items[1]);
                    while (num-- > 0) {
                        int msgId = paho.publish(topic, (body + num).getBytes(), 1, true);
                        System.err.println("publish " + msgId);
                    }
                } else {
                    int msgId = paho.publish(topic, line.getBytes(), 1, true);
                    System.err.println("publish " + msgId);
                }
            }
        }
        paho.disconnect();
        paho.close();
    }
}
