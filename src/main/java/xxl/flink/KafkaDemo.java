package xxl.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import xxl.kafka.Record;

public class KafkaDemo {
  public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.enableCheckpointing(5000);
    FKConsumer consumer = new FKConsumer(new String[]{"localhost:9094", "localhost:9095", "localhost:9096"}, "flink", "flink", false);
    consumer.setStartFromEarliest();
    FKProducer producer = new FKProducer(new String[]{"localhost:9094", "localhost:9095", "localhost:9096"}, -1, "xxl");
    producer.setWriteTimestampToKafka(true);
    env.addSource(consumer)
        .map(new MapFunction<Record, Record>() {
          @Override
          public Record map(Record value) throws Exception {
            return null;
          }
        })
        .addSink(producer);
    env.execute("KafkaDemo");
  }
}
