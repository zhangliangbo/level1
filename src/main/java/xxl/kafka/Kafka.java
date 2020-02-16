package xxl.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.ProduceResponse;
import xxl.mathematica.string.StringRiffle;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * kafka api
 */
public class Kafka {

  private static final String DefaultName = "xxl";
  private static Map<String, Consumer<String, String>> consumers = new HashMap<>();
  private static Map<String, Producer<String, String>> producers = new HashMap<>();

  /**
   * 新建一个生产者
   *
   * @param servers
   * @param ack
   * @return
   */
  public static String newProducer(String name, String[] servers, String ack) {
    if (producers.containsKey(name)) {
      return name;
    }
    Map<String, Object> props = new HashMap<>();
    props.put("bootstrap.servers", StringRiffle.stringRiffle(Arrays.asList(servers), ","));
    props.put("acks", ack);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    Producer<String, String> producer = new KafkaProducer<>(props);
    producers.put(name, producer);
    return name;
  }

  /**
   * 使用默认名称
   *
   * @param servers
   * @param ack
   * @return
   */
  public static String newProducer(String[] servers, String ack) {
    return newProducer(DefaultName, servers, ack);
  }

  /**
   * 使用默认分区策略
   * If a valid partition number is specified that partition will be used when sending the record. If no partition is
   * specified but a key is present a partition will be chosen using a hash of the key. If neither key nor partition is
   * present a partition will be assigned in a round-robin fashion.
   *
   * @param name
   * @param topic
   * @param key
   * @param value
   * @return
   */
  public static long send(String name, String topic, String key, String value) {
    return send(name, topic, null, key, value);
  }

  /**
   * 默认键为空
   *
   * @param name
   * @param topic
   * @param value
   * @return
   */
  public static long send(String name, String topic, String value) {
    return send(name, topic, null, value);
  }

  /**
   * 使用默认名称
   *
   * @param topic
   * @param value
   * @return
   */
  public static long send(String topic, String value) {
    return send(DefaultName, topic, value);
  }

  /**
   * 发送数据
   *
   * @param name
   * @param topic
   * @param partition
   * @param timestamp
   * @param key
   * @param value
   * @return 分区offset
   */
  public static long send(String name, String topic, Integer partition, Long timestamp, String key, String value) {
    Producer<String, String> producer = producers.get(name);
    if (producer != null) {
      ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, timestamp, key, value);
      Future<RecordMetadata> future = producer.send(record);
      try {
        RecordMetadata metadata = future.get();
        return metadata.offset();
      } catch (InterruptedException | ExecutionException e) {
        return ProduceResponse.INVALID_OFFSET;
      }
    }
    return ProduceResponse.INVALID_OFFSET;
  }

  /**
   * 时间默认使用System.currentTimeMillis()
   *
   * @param name
   * @param topic
   * @param partition
   * @param key
   * @param value
   * @return
   */
  public static long send(String name, String topic, Integer partition, String key, String value) {
    return send(name, topic, partition, null, key, value);
  }

  /**
   * 关闭生产者
   *
   * @param name
   */
  public static void closeProducer(String name) {
    if (producers.containsKey(name)) {
      producers.get(name).close();
      producers.remove(name);
    }
  }

  /**
   * 新建一个消费者
   *
   * @param name
   * @param servers
   * @param group
   * @param autoCommit
   * @return
   */
  public static String newConsumer(String name, String[] servers, String group, boolean autoCommit) {
    if (consumers.containsKey(name)) {
      return name;
    }
    Map<String, Object> props = new HashMap<>();
    props.put("bootstrap.servers", StringRiffle.stringRiffle(Arrays.asList(servers)));
    props.put("group.id", group);//指定消费者属于哪个组
    props.put("enable.auto.commit", String.valueOf(autoCommit));//开启kafka的offset自动提交功能，可以保证消费者数据不丢失
    props.put("auto.commit.interval.ms", "1000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumers.put(name, consumer);
    return name;
  }

  /**
   * 默认手动提交
   *
   * @param name
   * @param servers
   * @param group
   * @return 消费者名称
   */
  public static String newConsumer(String name, String[] servers, String group) {
    return newConsumer(name, servers, group, false);
  }

  /**
   * 使用默认消费者名称
   *
   * @param servers
   * @param group
   * @return
   */
  public static String newConsumer(String[] servers, String group) {
    return newConsumer(DefaultName, servers, group);
  }

  /**
   * 订阅主题
   *
   * @param name
   * @param topics
   */
  public static void subscribe(String name, String[] topics) {
    if (consumers.containsKey(name)) {
      consumers.get(name).subscribe(Arrays.asList(topics));
    }
  }

  /**
   * 订阅主题
   *
   * @param name
   * @param pattern
   */
  public static void subscribe(String name, String pattern) {
    if (consumers.containsKey(name)) {
      consumers.get(name).subscribe(Pattern.compile(pattern));
    }
  }

  /**
   * 取消订阅主题
   *
   * @param name
   */
  public static void unsubscribe(String name) {
    if (consumers.containsKey(name)) {
      consumers.get(name).unsubscribe();
    }
  }

  /**
   * 跳转
   *
   * @param name
   * @param topic
   */
  public static void seek(String name, String topic, Integer partition, long offset) {
    if (consumers.containsKey(name)) {
      consumers.get(name).seek(new TopicPartition(topic, partition), offset);
    }
  }

  /**
   * 跳转到分区开头
   *
   * @param name
   * @param topic
   * @param partition
   */
  public static void seekToBeginning(String name, String topic, Integer partition) {
    if (consumers.containsKey(name)) {
      consumers.get(name).seekToBeginning(Collections.singleton(new TopicPartition(topic, partition)));
    }
  }

  /**
   * 跳转到分区结尾
   *
   * @param name
   * @param topic
   * @param partition
   */
  public static void seekToEnd(String name, String topic, Integer partition) {
    if (consumers.containsKey(name)) {
      consumers.get(name).seekToEnd(Collections.singleton(new TopicPartition(topic, partition)));
    }
  }

  /**
   * 下一个数据的位置
   *
   * @param name
   * @param topic
   * @param partition
   * @return
   */
  public static long position(String name, String topic, Integer partition) {
    if (consumers.containsKey(name)) {
      return consumers.get(name).position(new TopicPartition(topic, partition));
    }
    return -1;
  }

  /**
   * 拉取服务器数据
   *
   * @param name
   * @param millis
   * @return
   */
  public static List<Record> poll(String name, long millis) {
    if (consumers.containsKey(name)) {
      ConsumerRecords<String, String> records = consumers.get(name).poll(Duration.ofMillis(millis));
      List<Record> res = new ArrayList<>();
      for (ConsumerRecord<String, String> record : records) {
        res.add(new Record(
            record.timestamp(),
            record.topic(),
            record.partition(),
            record.offset(),
            record.key(),
            record.value()
        ));
      }
      return res;
    }
    return null;
  }

  /**
   * 同步确认最近一次poll
   */
  public static void commitSync(String name) {
    if (consumers.containsKey(name)) {
      consumers.get(name).commitSync();
    }
  }

  /**
   * 指定topic/partition/offset
   *
   * @param name
   * @param topic
   * @param partition
   */
  public static void commitSync(String name, String topic, Integer partition, Long offset) {
    if (consumers.containsKey(name)) {
      Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
      map.put(new TopicPartition(topic, partition), new OffsetAndMetadata(offset));
      consumers.get(name).commitSync(map);
    }
  }

  /**
   * 唤醒消费者
   * 抛出异常WakeupException
   *
   * @param name
   */
  public static void wakeup(String name) {
    if (consumers.containsKey(name)) {
      consumers.get(name).wakeup();
    }
  }

  /**
   * 关闭消费者
   *
   * @param name
   */
  public static void closeConsumer(String name) {
    if (consumers.containsKey(name)) {
      consumers.get(name).close();
      consumers.remove(name);
    }
  }


}
