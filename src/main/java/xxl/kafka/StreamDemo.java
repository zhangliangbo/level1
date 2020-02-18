package xxl.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.ValueMapperWithKey;

import java.util.Properties;

public class StreamDemo {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kou");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094,localhost:9095,localhost:9096");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    builder.<String, String>stream("kou").mapValues(new ValueMapperWithKey<String, String, String>() {
      @Override
      public String apply(String readOnlyKey, String value) {
        return readOnlyKey + "---" + value;
      }
    }).to("xxl");
    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
  }
}
