package xxl.kafka;

/**
 * 发送结果
 */
public class SendResult {
  private Long time;
  private String topic;
  private Integer partition;
  private Long offset;

  public SendResult(Long time, String topic, Integer partition, Long offset) {
    this.time = time;
    this.topic = topic;
    this.partition = partition;
    this.offset = offset;
  }

  public Long time() {
    return time;
  }

  public String topic() {
    return topic;
  }

  public Integer partition() {
    return partition;
  }

  public Long offset() {
    return offset;
  }

  @Override
  public String toString() {
    return "SendResult{" +
        "time=" + time +
        ", topic='" + topic + '\'' +
        ", partition=" + partition +
        ", offset=" + offset +
        '}';
  }
}
