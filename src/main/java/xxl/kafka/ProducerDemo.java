package xxl.kafka;

/**
 * 消息生产者
 */
public class ProducerDemo {
  public static void main(String[] args) {
    String producer = Kafka.newProducer(new String[]{"localhost:9094"}, "all");
    for (int i = 0; i < 100; i++) {
      long offset = Kafka.send(producer, "test", "hello" + i);
      System.err.println("offset is " + offset);
    }
    Kafka.closeProducer(producer);
  }
}
