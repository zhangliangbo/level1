package xxl.flink;

import com.rabbitmq.client.AMQP;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSink;
import org.apache.flink.streaming.connectors.rabbitmq.SerializableReturnListener;
import xxl.rabbitmq.Record;
import xxl.rabbitmq.Return;
import xxl.rabbitmq.ReturnConsumer;

import java.io.IOException;

/**
 * rabbitmq生产者
 */
public class FRProducer extends RMQSink<Record> {
  private String exchange;
  private String type;

  /**
   * 发送到交换器
   *
   * @param host
   * @param port
   * @param username
   * @param password
   * @param vHost
   * @param mandatory
   * @param immediate
   * @param consumer
   */
  public FRProducer(String host, Integer port, String username, String password, String vHost, String exchange, String type, boolean mandatory, boolean immediate, ReturnConsumer consumer) {
    super(FRUtil.config(host, port, username, password, vHost), new FRSerialization(), new FRPublishOptions(mandatory, immediate), new SerializableReturnListener() {
      @Override
      public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
        if (consumer != null) {
          consumer.onReturn(new Return(replyCode, replyText, exchange, routingKey, body));
        }
      }
    });
    this.exchange = exchange;
    this.type = type;
  }

  /**
   * 发送到交换器，非强制性
   *
   * @param host
   * @param port
   * @param username
   * @param password
   * @param vHost
   */
  public FRProducer(String host, Integer port, String username, String password, String vHost, String exchange, String type) {
    this(host, port, username, password, vHost, exchange, type, false, false, null);
  }

  /**
   * 发送到队列
   *
   * @param host
   * @param port
   * @param username
   * @param password
   * @param vHost
   * @param queueName
   */
  public FRProducer(String host, Integer port, String username, String password, String vHost, String queueName) {
    super(FRUtil.config(host, port, username, password, vHost), queueName, new FRSerialization());
  }


  @Override
  protected void setupQueue() throws IOException {
    if (exchange != null) {
      channel.exchangeDeclare(exchange, type, true, false, null);
    }
    if (queueName != null) {
      channel.queueDeclare(queueName, true, false, false, null);
    }
  }
}
