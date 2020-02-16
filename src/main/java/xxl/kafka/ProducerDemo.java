package xxl.kafka;

/**
 * 消息生产者
 */
public class ProducerDemo {
  public static void main(String[] args) {
    KfkProducer producer = new KfkProducer(new String[]{"localhost:9094"}, "all");
    for (int i = 0; i < 100; i++) {
      long offset = producer.send("test", "key" + i, "hello" + i);
      System.err.println("offset is " + offset);
    }
    producer.close();
  }
}
