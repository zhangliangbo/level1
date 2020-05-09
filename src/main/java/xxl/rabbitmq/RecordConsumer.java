package xxl.rabbitmq;

public interface RecordConsumer {
    void onDelivery(Record record);
}
