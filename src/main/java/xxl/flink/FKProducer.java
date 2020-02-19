package xxl.flink;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import xxl.kafka.KfkProducer;
import xxl.kafka.Record;

import java.util.Properties;

/**
 * 生产者
 */
public class FKProducer extends FlinkKafkaProducer<Record> {
  public FKProducer(String[] servers, int ack, String topic) {
    super(topic, new FKSerialization(), KfkProducer.props(servers, ack),
        ack == 0 ? Semantic.NONE : (ack == -1 ? Semantic.EXACTLY_ONCE : Semantic.AT_LEAST_ONCE));
  }

  private static Properties props(String[] servers, int ack) {
    Properties props = KfkProducer.props(servers, ack);
    props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    return props;
  }
}
