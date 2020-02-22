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
      if (rabbitMQ.confirmSelect()) {//打开消息确认模式
        for (int i = 0; i < 1000; i++) {
          if (rabbitMQ.publish("exchange", "routingKey", ("hello" + i).getBytes())) {
            System.err.println("send hello " + i);
            if (rabbitMQ.waitForConfirms()) {
              System.err.println("confirmed");
            } else {
              System.err.println("not confirmed");
            }
          }
        }
      }
    }
    rabbitMQ.close();
    System.err.println("finished");
  }
}
