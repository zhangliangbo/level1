package xxl.flink;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;
import xxl.rabbitmq.Record;

import java.io.IOException;

public class FRDeserialization implements DeserializationSchema<Record> {
  @Override
  public Record deserialize(byte[] message) throws IOException {
    return new Record(message, null, null, null, 0, false);
  }

  @Override
  public boolean isEndOfStream(Record nextElement) {
    return false;
  }

  @Override
  public TypeInformation<Record> getProducedType() {
    return new GenericTypeInfo<>(Record.class);
  }
}
