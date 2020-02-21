package xxl.rabbitmq;

public class QueueDemo {
  public static void main(String[] args) {
    RabbitMQ rabbitMQ = new RabbitMQ(
        new String[]{"localhost:5672"},
        "mqtt",
        "mqtt",
        "/",
        true
    );
    if (rabbitMQ.newChannel() && rabbitMQ.exchangeDeclare("xxl") && rabbitMQ.queueDeclare("zlb") && rabbitMQ.queueBind("zlb", "xxl", "xxl-zlb")) {
      while (true) {
        if (rabbitMQ.qos(1, false)) {
          Record record = rabbitMQ.get("zlb", false);
          System.err.println("get " + new String(record.body()));
          rabbitMQ.ack(record.deliveryTag(), false);
        }
      }
    }
  }
}
