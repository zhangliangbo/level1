package xxl.flink;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import xxl.kafka.Record;

import javax.annotation.Nullable;

public class FKSerialization implements KafkaSerializationSchema<Record> {

    @Override
    public ProducerRecord<byte[], byte[]> serialize(Record element, @Nullable Long timestamp) {
        return new ProducerRecord<>(
                element.topic(),
                element.partition(),
                timestamp,
                element.key(),
                element.key()
        );
    }
}
