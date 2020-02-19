package xxl.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import xxl.kafka.Record;

public class KafkaDemo {
  public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.enableCheckpointing(5000);
    FKConsumer consumer = new FKConsumer(new String[]{"kafka1:9092", "kafka2:9092", "kafka3:9092"}, "flink", "flink", true);
    consumer.setStartFromEarliest();
    FKProducer producer = new FKProducer(new String[]{"kafka1:9092", "kafka2:9092", "kafka3:9092"}, -1, "xxl");
    producer.setWriteTimestampToKafka(true);
    env.addSource(consumer)
        .map(new MapFunction<Record, Record>() {
          @Override
          public Record map(Record value) throws Exception {
            return new Record("xxl", value.key() + " flink", value.value() + " flink");
          }
        })
        .addSink(producer);
    env.execute("KafkaDemo");
  }
}
