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
    if (rabbitMQ.newChannel()) {
      if (rabbitMQ.exchangeDeclare("exchange", "direct", true, false) && rabbitMQ.queueDeclare("queue", true, false, false) && rabbitMQ.queueBind("queue", "exchange", "routingKey")) {
        if (rabbitMQ.qos(1, false)) {
          rabbitMQ.consume("queue", "random", new RabbitConsumer() {
            @Override
            public void onDelivery(Record record) {
              if (record != null) {
                System.err.println(record);
                rabbitMQ.ack(record.deliveryTag(), false);
              }
            }
          });
//          while (true) {
//            Record record = rabbitMQ.get("queue", false);
//            if (record != null) {
//              System.err.println(record);
//              if (rabbitMQ.ack(record.deliveryTag(), false)) {
//                System.err.println("acknowledgement");
//              }
//            }
//          }
        }
      }
    }
  }
}
