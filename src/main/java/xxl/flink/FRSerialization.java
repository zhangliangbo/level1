package xxl.flink;

import org.apache.flink.api.common.serialization.SerializationSchema;
import xxl.rabbitmq.Record;

public class FRSerialization implements SerializationSchema<Record> {
    @Override
    public byte[] serialize(Record element) {
        return element.body();
    }
}
