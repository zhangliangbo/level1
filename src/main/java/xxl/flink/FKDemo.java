package xxl.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import xxl.kafka.Record;

public class FKDemo {
  public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    FKConsumer consumer = new FKConsumer(new String[]{"kafka1:9092", "kafka2:9092", "kafka3:9092"}, "kou", "flink", true);
    FKProducer producer = new FKProducer(new String[]{"kafka1:9092", "kafka2:9092", "kafka3:9092"}, -1, "xxl");
    env.addSource(consumer)
        .filter(new FilterFunction<Record>() {
          @Override
          public boolean filter(Record value) throws Exception {
            return new String(value.value()).contains("0");
          }
        })
        .map(new MapFunction<Record, Record>() {
          @Override
          public Record map(Record value) throws Exception {
            return new Record("xxl", value.key(), value.value());
          }
        })
        .addSink(producer);
    env.execute("KafkaDemo");
  }
}
