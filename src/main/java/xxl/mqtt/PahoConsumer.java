package xxl.mqtt;

public interface PahoConsumer {
  void onMessage(PahoMessage message);
}
