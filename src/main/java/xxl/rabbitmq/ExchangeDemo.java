package xxl.rabbitmq;

public class ExchangeDemo {
  public static void main(String[] args) {
    RabbitMQ rabbitMQ = new RabbitMQ(
        new String[]{"localhost:5672"},
        "mqtt",
        "mqtt",
        "/",
        true
    );
    if (rabbitMQ.newChannel()) {
      if (rabbitMQ.exchangeDeclare("exchange", "direct", true, false)) {
        for (int i = 0; i < 1000; i++) {
          if (rabbitMQ.publish("exchange", "routingKey", ("hello" + i).getBytes())) {
            System.err.println("send hello " + i);
          }
        }
      }
    }
    rabbitMQ.close();
    System.err.println("finished");
  }
}
