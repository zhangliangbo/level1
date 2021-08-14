package xxl.kafka;

/**
 * 消息生产者
 */
public class ProducerDemo {
    public static void main(String[] args) {
        KfkProducer producer = new KfkProducer(new String[]{"localhost:9092"}, -1);
        for (int i = 0; i < 100; i++) {
            SendResult result = producer.send("kou", ("key" + i).getBytes(), ("hello" + i).getBytes());
            System.err.println(result);
        }
        producer.close();
    }
}
