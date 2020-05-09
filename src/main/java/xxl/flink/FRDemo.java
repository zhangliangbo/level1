package xxl.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import xxl.rabbitmq.Record;

public class FRDemo {
    public static void main(String[] args) throws Exception {
        FRConsumer consumer = new FRConsumer("localhost", 5672, "mqtt", "mqtt", "/",
                "fr-queue", "amq.direct", "direct", "to-flink");
        FRProducer producer = new FRProducer("localhost", 5672, "mqtt", "mqtt", "/",
                "amq.direct", "direct");
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.addSource(consumer)
                .filter(new FilterFunction<Record>() {
                    @Override
                    public boolean filter(Record value) throws Exception {
                        return new String(value.body()).contains("0");
                    }
                })
                .map(new MapFunction<Record, Record>() {
                    @Override
                    public Record map(Record value) throws Exception {
                        return new Record((new String(value.body()) + " powered by flink").getBytes(), "amq.direct", "from-flink", "fr-consumer", 0, false);
                    }
                })
                .addSink(producer);
        environment.execute("FRDemo");
    }
}
