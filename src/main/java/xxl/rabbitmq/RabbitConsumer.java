package xxl.rabbitmq;

public interface RabbitConsumer {
  void onDelivery(Record record);
}
