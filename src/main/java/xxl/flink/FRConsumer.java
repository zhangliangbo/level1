package xxl.flink;

import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import xxl.rabbitmq.Record;

/**
 * rabbitmq消费者
 */
public class FRConsumer extends RMQSource<Record> {
  public FRConsumer(String host, Integer port, String username, String password, String vHost, String queueName) {
    super(FRUtil.config(host, port, username, password, vHost), queueName, new FRDeserialization());
  }
}
