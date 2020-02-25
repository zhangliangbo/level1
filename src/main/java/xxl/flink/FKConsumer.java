package xxl.flink;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import xxl.kafka.KfkConsumer;
import xxl.kafka.Record;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 消费者
 */
public class FKConsumer extends FlinkKafkaConsumer<Record> {

  public FKConsumer(String[] servers, String[] topics, String group, boolean autoCommit) {
    super(Arrays.asList(topics), new FKDeserialization(), KfkConsumer.props(servers, group, autoCommit));
  }

  public FKConsumer(String[] servers, String regex, String group, boolean autoCommit) {
    super(Pattern.compile(regex), new FKDeserialization(), KfkConsumer.props(servers, group, autoCommit));
  }

}
