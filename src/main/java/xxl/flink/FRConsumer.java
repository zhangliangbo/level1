package xxl.flink;

import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import xxl.rabbitmq.Record;

import java.io.IOException;

/**
 * rabbitmq消费者
 */
public class FRConsumer extends RMQSource<Record> {

  private String exchange;
  private String type;
  private String routingKey;

  public FRConsumer(String host, Integer port, String username, String password, String vHost, String queueName, String exchange, String type, String routingKey) {
    super(FRUtil.config(host, port, username, password, vHost), queueName, new FRDeserialization());
    this.exchange = exchange;
    this.type = type;
    this.routingKey = routingKey;
  }

  @Override
  protected void setupQueue() throws IOException {
    super.setupQueue();
    if (exchange != null) {
      channel.exchangeDeclare(exchange, type, true, false, null);
      channel.queueBind(queueName, exchange, routingKey);
    }
  }
}
