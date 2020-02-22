package xxl.rabbitmq;

public interface ReturnConsumer {
  void onReturn(Return r);
}
