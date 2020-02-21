package xxl.rabbitmq;

import java.util.Arrays;

public class Record {
  private byte[] body;
  private String exchange;
  private String routingKey;
  private String consumerTag;
  private long deliveryTag;
  private boolean redeliver;

  public Record(byte[] body, String exchange, String routingKey, String consumerTag, long deliveryTag, boolean redeliver) {
    this.body = body;
    this.exchange = exchange;
    this.routingKey = routingKey;
    this.consumerTag = consumerTag;
    this.deliveryTag = deliveryTag;
    this.redeliver = redeliver;
  }

  public byte[] body() {
    return body;
  }

  public String exchange() {
    return exchange;
  }

  public String routingKey() {
    return routingKey;
  }

  public String consumerTag() {
    return consumerTag;
  }

  public long deliveryTag() {
    return deliveryTag;
  }

  public boolean redeliver() {
    return redeliver;
  }

  @Override
  public String toString() {
    return "Record{" +
        "body=" + Arrays.toString(body) +
        ", exchange='" + exchange + '\'' +
        ", routingKey='" + routingKey + '\'' +
        ", consumerTag='" + consumerTag + '\'' +
        ", deliveryTag=" + deliveryTag +
        ", redeliver=" + redeliver +
        '}';
  }
}
