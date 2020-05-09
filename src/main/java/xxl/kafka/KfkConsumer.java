package xxl.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import xxl.mathematica.string.StringRiffle;

import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 消费者
 */
public class KfkConsumer {
    private final Consumer<byte[], byte[]> consumer;

    public KfkConsumer(String[] servers, String group, boolean autoCommit) {
        consumer = new KafkaConsumer<>(props(servers, group, autoCommit));
    }

    /**
     * 默认手动提交
     *
     * @param servers
     * @param group
     * @return 消费者名称
     */
    public KfkConsumer(String[] servers, String group) {
        this(servers, group, false);
    }

    /**
     * 通用属性
     *
     * @param servers
     * @param group
     * @param autoCommit
     * @return
     */
    public static Properties props(String[] servers, String group, boolean autoCommit) {
        Properties props = new Properties();
        props.put("bootstrap.servers", StringRiffle.stringRiffle(Arrays.asList(servers)));
        props.put("group.id", group);//指定消费者属于哪个组
        props.put("enable.auto.commit", String.valueOf(autoCommit));//开启kafka的offset自动提交功能，可以保证消费者数据不丢失
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        return props;
    }

    /**
     * 订阅主题
     *
     * @param topics
     */
    public void subscribe(String[] topics) {
        consumer.subscribe(Arrays.asList(topics));
    }

    /**
     * 订阅主题
     *
     * @param pattern
     */
    public void subscribe(String pattern) {
        consumer.subscribe(Pattern.compile(pattern));
    }

    /**
     * 分配分区
     *
     * @param topic
     * @param partition
     */
    public void assign(String topic, Integer partition) {
        consumer.assign(Collections.singleton(new TopicPartition(topic, partition)));
    }

    /**
     * 取消订阅主题
     */
    public void unsubscribe(String name) {
        consumer.unsubscribe();
    }

    /**
     * 跳转
     *
     * @param topic
     */
    public void seek(String topic, Integer partition, long offset) {
        consumer.seek(new TopicPartition(topic, partition), offset);
    }

    /**
     * 跳转到分区开头
     *
     * @param topic
     * @param partition
     */
    public void seekToBeginning(String topic, Integer partition) {
        consumer.seekToBeginning(Collections.singleton(new TopicPartition(topic, partition)));
    }

    /**
     * 跳转到分区结尾
     *
     * @param topic
     * @param partition
     */
    public void seekToEnd(String topic, Integer partition) {
        consumer.seekToEnd(Collections.singleton(new TopicPartition(topic, partition)));
    }

    /**
     * 下一个数据的位置
     *
     * @param topic
     * @param partition
     * @return
     */
    public long position(String topic, Integer partition) {
        return consumer.position(new TopicPartition(topic, partition));
    }

    /**
     * 拉取服务器数据
     *
     * @param millis
     * @return
     */
    public List<Record> poll(long millis) {
        ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofMillis(millis));
        List<Record> res = new ArrayList<>();
        for (ConsumerRecord<byte[], byte[]> record : records) {
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

    /**
     * 同步确认最近一次poll
     */
    public void commitSync() {
        consumer.commitSync();
    }

    /**
     * 指定topic/partition/offset
     *
     * @param topic
     * @param partition
     */
    public void commitSync(String topic, Integer partition, Long offset) {
        Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
        map.put(new TopicPartition(topic, partition), new OffsetAndMetadata(offset));
        consumer.commitSync(map);
    }

    /**
     * 唤醒消费者
     * 抛出异常WakeupException
     */
    public void wakeup() {
        consumer.wakeup();
    }

    /**
     * 关闭消费者
     */
    public void close() {
        consumer.close();
    }

}
