package xxl.flink;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import xxl.kafka.KfkProducer;
import xxl.kafka.Record;

/**
 * 生产者
 */
public class FKProducer extends FlinkKafkaProducer<Record> {
    public FKProducer(String[] servers, int ack, String topic) {
        super(topic, new FKSerialization(), KfkProducer.props(servers, ack),
                ack == 0 ? Semantic.NONE : (ack == -1 ? Semantic.EXACTLY_ONCE : Semantic.AT_LEAST_ONCE));
    }
}
