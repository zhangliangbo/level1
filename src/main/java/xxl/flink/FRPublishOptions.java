package xxl.flink;

import com.rabbitmq.client.AMQP;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSinkPublishOptions;
import xxl.rabbitmq.Record;

/**
 * 发布消息的参数
 */
class FRPublishOptions implements RMQSinkPublishOptions<Record> {

    private boolean mandatory;
    private boolean immediate;

    public FRPublishOptions(boolean mandatory, boolean immediate) {
        this.mandatory = mandatory;
        this.immediate = immediate;
    }

    public FRPublishOptions() {
        this(false, false);
    }

    @Override
    public String computeRoutingKey(Record a) {
        return a.routingKey();
    }

    @Override
    public AMQP.BasicProperties computeProperties(Record a) {
        return null;
    }

    @Override
    public String computeExchange(Record a) {
        return a.exchange();
    }

    @Override
    public boolean computeMandatory(Record a) {
        return mandatory;
    }

    @Override
    public boolean computeImmediate(Record a) {
        return immediate;
    }
}
