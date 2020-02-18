package xxl.flink;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import xxl.kafka.Record;

public class FKDeserialization implements KafkaDeserializationSchema<Record> {
  @Override
  public boolean isEndOfStream(Record nextElement) {
    return false;
  }

  @Override
  public Record deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
    return new Record(
        record.timestamp(),
        record.topic(),
        record.partition(),
        record.offset(),
        new String(record.key()),
        new String(record.value())
    );
  }

  @Override
  public TypeInformation<Record> getProducedType() {
    return new GenericTypeInfo<>(Record.class);
  }
}
