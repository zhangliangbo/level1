package xxl.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import xxl.kafka.Record;

public class KafkaDemo {
  public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    FKConsumer consumer = new FKConsumer(new String[]{"localhost:9094", "localhost:9095", "localhost:9096"}, "kou", "flink", true);
    FKProducer producer = new FKProducer(new String[]{"localhost:9094", "localhost:9095", "localhost:9096"}, -1, "xxl");
    env.addSource(consumer)
        .map(new MapFunction<Record, Record>() {
          @Override
          public Record map(Record value) throws Exception {
            System.err.println("get a " + value.key() + ":" + value.value());
            return new Record("xxl", value.key() + " flink", value.value() + " flink");
          }
        })
        .addSink(producer);
    env.execute("KafkaDemo");
  }
}
