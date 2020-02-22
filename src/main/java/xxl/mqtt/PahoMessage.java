package xxl.mqtt;

public class PahoMessage {
  private String topic;
  private byte[] body;
  private int qos;
  private int id;
  private boolean retain;
  private boolean duplicate;

  public PahoMessage(String topic, byte[] body, int qos, int id, boolean retain, boolean duplicate) {
    this.topic = topic;
    this.body = body;
    this.qos = qos;
    this.id = id;
    this.retain = retain;
    this.duplicate = duplicate;
  }

  public String topic() {
    return topic;
  }

  public byte[] body() {
    return body;
  }

  public int qos() {
    return qos;
  }

  public int id() {
    return id;
  }

  public boolean retain() {
    return retain;
  }

  public boolean duplicate() {
    return duplicate;
  }
}
