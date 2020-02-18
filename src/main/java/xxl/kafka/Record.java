package xxl.kafka;

public class Record {
  private Long time;
  private String topic;
  private Integer partition;
  private Long offset;
  private String key;
  private String value;

  public Record(Long time, String topic, Integer partition, Long offset, String key, String value) {
    this.time = time;
    this.topic = topic;
    this.partition = partition;
    this.offset = offset;
    this.key = key;
    this.value = value;
  }

  public Record(String topic, String key, String value) {
    this(null, topic, null, null, key, value);
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

  public String key() {
    return key;
  }

  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return "Record{" +
        "time=" + time +
        ", topic='" + topic + '\'' +
        ", partition=" + partition +
        ", offset=" + offset +
        ", key='" + key + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
