package xxl.rabbitmq;

import com.rabbitmq.client.*;
import xxl.mathematica.Map;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq连接
 */
public class RabbitMQ {
  private Connection connection;
  private Channel channel;

  public RabbitMQ(String[] servers, String username, String password, String vHost) throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUsername(username);
    factory.setPassword(password);
    factory.setVirtualHost(vHost);
    connection = factory.newConnection(Map.map(t -> {
      String[] server = t.split(":");
      if (server.length < 2) {
        return new Address(server[0]);
      } else {
        return new Address(server[0], Integer.parseInt(server[1]));
      }
    }, Arrays.asList(servers)));
    channel = connection.createChannel();
  }

  /**
   * 声明交换器
   *
   * @param exchange
   * @return
   */
  public boolean exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete) {
    try {
      channel.exchangeDeclare(exchange, type, durable, autoDelete, null);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 默认没有绑定时（先绑过，后来没绑了），自动删除
   *
   * @param exchange
   * @param type
   * @param durable
   * @return
   */
  public boolean exchangeDeclare(String exchange, String type, boolean durable) {
    return exchangeDeclare(exchange, type, durable, true);
  }

  /**
   * 默认不持久化
   *
   * @param exchange
   * @param type
   * @return
   */
  public boolean exchangeDeclare(String exchange, String type) {
    return exchangeDeclare(exchange, type, false);
  }

  /**
   * 默认direct类型
   *
   * @param exchange
   * @return
   */
  public boolean exchangeDeclare(String exchange) {
    return exchangeDeclare(exchange, "direct");
  }

  /**
   * 交换器是否存在
   *
   * @param exchange
   * @return
   */
  public boolean exchangeExist(String exchange) {
    try {
      channel.exchangeDeclarePassive(exchange);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 声明一个队列
   *
   * @param queue
   * @param durable
   * @param exclusive
   * @param autoDelete
   * @return
   */
  public boolean queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete) {
    try {
      channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 默认自动删除
   *
   * @param queue
   * @param durable
   * @param exclusive
   * @return
   */
  public boolean queueDeclare(String queue, boolean durable, boolean exclusive) {
    return queueDeclare(queue, durable, exclusive, true);
  }

  /**
   * 默认不绑定连接
   *
   * @param queue
   * @param durable
   * @return
   */
  public boolean queueDeclare(String queue, boolean durable) {
    return queueDeclare(queue, durable, false);
  }

  /**
   * 不持久化
   *
   * @param queue
   * @return
   */
  public boolean queueDeclare(String queue) {
    return queueDeclare(queue, false);
  }

  /**
   * 队列是否存在
   *
   * @param queue
   * @return
   */
  public boolean queueExist(String queue) {
    try {
      AMQP.Queue.DeclareOk res = channel.queueDeclarePassive(queue);
      return true;
    } catch (IOException e) {
      return false;
    }
  }


  public void close() throws IOException, TimeoutException {
    channel.close();
    connection.close();
  }
}
