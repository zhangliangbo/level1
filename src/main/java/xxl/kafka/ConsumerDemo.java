package xxl.kafka;

import java.util.List;

public class ConsumerDemo {
  public static void main(String[] args) {
    String consumer = Kafka.newConsumer(new String[]{"localhost:9094"}, "test");
    Kafka.subscribe(consumer, "test");
    while (true) {
      List<Record> records = Kafka.poll(consumer, 1000);
      if (records != null) {
        for (Record record : records) {
          System.err.println(record);
          Kafka.commitSync(consumer);
        }
      }
    }
  }
}
