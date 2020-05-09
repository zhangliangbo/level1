package xxl.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.ValueMapperWithKey;

public class StreamDemo {
    public static void main(String[] args) {
        StreamsBuilder builder = new StreamsBuilder();
        builder.<String, String>stream("kou").mapValues(new ValueMapperWithKey<String, String, String>() {
            @Override
            public String apply(String readOnlyKey, String value) {
                System.err.println(readOnlyKey + " " + value);
                return readOnlyKey + "---" + value;
            }
        }).to("xxl");
        KafkaStreams streams = new KafkaStreams(builder.build(), KfkStream.props(new String[]{"localhost:9094", "localhost:9095", "localhost:9096"}, "kou"));
        streams.start();
    }
}
