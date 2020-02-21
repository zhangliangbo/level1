package xxl.rabbitmq;

public interface RabbitConsumer {
  void onDelivery(byte[] body, String exchange, String routingKey, String consumerTag, long deliveryTag, boolean redeliver);
}
