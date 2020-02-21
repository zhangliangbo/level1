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
    if (rabbitMQ.newChannel() && rabbitMQ.exchangeDeclare("xxl")) {
      for (int i = 0; i < 100; i++) {
        if (rabbitMQ.publish("xxl", "xxl-zlb", ("hello" + i).getBytes())) {
          System.err.println("send hello " + i);
        }
      }
    }
//    rabbitMQ.close();
    System.err.println("finished");
  }
}
