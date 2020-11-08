package xxl.rabbitmq;

import com.rabbitmq.client.*;
import xxl.mathematica.functional.Map;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq连接
 */
public class RabbitMQ implements ReturnListener, ShutdownListener {
    private ConnectionFactory factory;
    private String[] servers;
    private Connection connection;
    private Channel channel;
    private ReturnConsumer returnConsumer;

    /**
     * 构造函数
     *
     * @param servers        host:port
     * @param username       用户名
     * @param password       密码
     * @param vHost          虚拟机
     * @param autoRecovery   自动回复
     * @param returnConsumer 无法投递的消息接收器
     */
    public RabbitMQ(String[] servers, String username, String password, String vHost, boolean autoRecovery, ReturnConsumer returnConsumer) {
        this.servers = servers;
        this.factory = new ConnectionFactory();
        this.factory.setUsername(username);
        this.factory.setPassword(password);
        this.factory.setVirtualHost(vHost);
        this.factory.setAutomaticRecoveryEnabled(autoRecovery);
        this.returnConsumer = returnConsumer;
    }

    public RabbitMQ(String[] servers, String username, String password, String vHost, boolean autoRecovery) {
        this(servers, username, password, vHost, autoRecovery, null);
    }

    /**
     * 建立通道
     *
     * @return 成功或失败
     */
    public boolean newChannel() {
        try {
            if (connection == null) {
                connection = factory.newConnection(Map.map(t -> {
                    String[] server = t.split(":");
                    if (server.length < 2) {
                        return new Address(server[0]);
                    } else {
                        return new Address(server[0], Integer.parseInt(server[1]));
                    }
                }, Arrays.asList(servers)));
            }
            channel = connection.createChannel();
            channel.addReturnListener(this);
            channel.addShutdownListener(this);
            return true;
        } catch (IOException | TimeoutException e) {
            return false;
        }
    }

    /**
     * 声明交换器
     * 类型决定转发规则
     * fanout，不处理路由键，只有绑定到交换器上就会完成转发
     * direct, 处理路由键，且路由键完全匹配才会转发
     * topic, 处理路由键，支持通配符，#代表一个或多词（以.分隔），*代表不多不少一个词
     *
     * @param exchange   交换器
     * @param type       类型
     * @param durable    持久化
     * @param autoDelete 自动删除
     * @return 成功或失败
     */
    public boolean exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete) {
        try {
            channel.exchangeDeclare(exchange, type, durable, autoDelete, null);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 不自动删除
     *
     * @param exchange 交换器
     * @param type     类型
     * @param durable  持久化
     * @return 成功或失败
     */
    public boolean exchangeDeclare(String exchange, String type, boolean durable) {
        return exchangeDeclare(exchange, type, durable, false);
    }

    /**
     * 默认持久化
     *
     * @param exchange 交换器
     * @param type     类型
     * @return 成功或失败
     */
    public boolean exchangeDeclare(String exchange, String type) {
        return exchangeDeclare(exchange, type, true);
    }

    /**
     * 默认topic类型
     *
     * @param exchange 交换器名称
     * @return 成功或失败
     */
    public boolean exchangeDeclare(String exchange) {
        return exchangeDeclare(exchange, BuiltinExchangeType.TOPIC.getType());
    }

    /**
     * 删除交换器
     *
     * @param exchange 交换器名称
     * @param ifUnused 是否不在使用中
     * @return 成功或失败
     */
    public boolean exchangeDelete(String exchange, boolean ifUnused) {
        try {
            channel.exchangeDelete(exchange, ifUnused);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 声明一个队列
     *
     * @param queue      队列名称
     * @param durable    是否持久化
     * @param exclusive  是否被连接独占
     * @param autoDelete 是否自动删除
     * @return 成功或失败
     */
    public boolean queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete) {
        try {
            channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 默认不自动删除
     *
     * @param queue     队列名称
     * @param durable   是否持久化
     * @param exclusive 是否被连接独占
     * @return 成功或失败
     */
    public boolean queueDeclare(String queue, boolean durable, boolean exclusive) {
        return queueDeclare(queue, durable, exclusive, false);
    }

    /**
     * 默认不被连接独占
     *
     * @param queue   队列名称
     * @param durable 是否持久化
     * @return 成功或失败
     */
    public boolean queueDeclare(String queue, boolean durable) {
        return queueDeclare(queue, durable, false);
    }

    /**
     * 默认持久化
     *
     * @param queue 队列名称
     * @return 成功或失败
     */
    public boolean queueDeclare(String queue) {
        return queueDeclare(queue, true);
    }

    /**
     * 删除队列
     *
     * @param queue    队列名称
     * @param ifUnused 是否不在使用中
     * @param ifEmpty  是否为空
     * @return 成功或失败
     */
    public boolean queueDelete(String queue, boolean ifUnused, boolean ifEmpty) {
        try {
            channel.queueDelete(queue, ifUnused, ifEmpty);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 交换器绑定交换器
     *
     * @param dstExchange 目标交换器
     * @param srcExchange 源交换器
     * @param routingKey  路由键
     * @return 成功或失败
     */
    public boolean exchangeBind(String dstExchange, String srcExchange, String routingKey) {
        try {
            channel.exchangeBind(dstExchange, srcExchange, routingKey);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 绑定器解绑绑定器
     *
     * @param dstExchange 目标交换器
     * @param srcExchange 源交换器
     * @param routingKey  路由键
     * @return 成功或失败
     */
    public boolean exchangeUnbind(String dstExchange, String srcExchange, String routingKey) {
        try {
            channel.exchangeUnbind(dstExchange, srcExchange, routingKey);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 队列绑定交换器
     *
     * @param queue      队列名称
     * @param exchange   交换器名称
     * @param routingKey 路由键
     * @return 成功或失败
     */
    public boolean queueBind(String queue, String exchange, String routingKey) {
        try {
            channel.queueBind(queue, exchange, routingKey);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 队列解绑交换器
     *
     * @param queue      队列名称
     * @param exchange   交换器名称
     * @param routingKey 路由键
     * @return 成功或失败
     */
    public boolean queueUnbind(String queue, String exchange, String routingKey) {
        try {
            channel.queueUnbind(queue, exchange, routingKey);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 清空队列
     *
     * @param queue 队列名称
     * @return 成功或失败
     */
    public boolean queuePurge(String queue) {
        try {
            channel.queuePurge(queue);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 发布消息
     * 默认强制，即未投递成功会打回
     *
     * @return 成功或失败
     */
    public boolean publish(String exchange, String routingKey, byte[] body) {
        return publish(exchange, routingKey, body, true);
    }

    /**
     * 发布消息
     *
     * @return 成功或失败
     */
    public boolean publish(String exchange, String routingKey, byte[] body, boolean mandatory) {
        return publish(exchange, routingKey, body, mandatory, false);
    }

    /**
     * 发布消息
     *
     * @return 成功或失败
     */
    public boolean publish(String exchange, String routingKey, byte[] body, boolean mandatory, boolean immediate) {
        try {
            channel.basicPublish(exchange, routingKey, mandatory, immediate, null, body);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 开启发布者确认模式
     *
     * @return 成功或失败
     */
    public boolean confirmSelect() {
        try {
            channel.confirmSelect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 等待应答
     *
     * @return 成功或失败
     */
    public boolean waitForConfirms() {
        try {
            return channel.waitForConfirms();
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 等待发布消息的应答
     *
     * @param timeout 超时时间
     * @return 成功或失败
     */
    public boolean waitForConfirms(long timeout) {
        try {
            return channel.waitForConfirms(timeout);
        } catch (InterruptedException | TimeoutException e) {
            return false;
        }
    }

    /**
     * 控制消费速度
     *
     * @param prefetchSize  预取大小
     * @param prefetchCount 预取数量
     * @param global        true表示整个通道，false表示单个消费者
     * @return 成功或失败
     */
    public boolean qos(int prefetchSize, int prefetchCount, boolean global) {
        try {
            channel.basicQos(prefetchCount, global);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取一个消息
     *
     * @param queue   队列名称
     * @param autoAck 自动ack
     * @return 消息
     */
    public Record get(String queue, boolean autoAck) {
        try {
            GetResponse res = channel.basicGet(queue, autoAck);
            if (res != null) {
                return new Record(res.getBody(), res.getEnvelope().getExchange(), res.getEnvelope().getRoutingKey(), null, res.getEnvelope().getDeliveryTag(), res.getEnvelope().isRedeliver());
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 消费消息
     *
     * @param queue     队列名称
     * @param autoAck   是否自动回复
     * @param tag       消费者标签
     * @param noLocal   不消费同一个连接发的消息
     * @param exclusive 是否接受其他连接的消费者
     * @param consumer  消费者
     * @return 成功或失败
     */
    public boolean consume(String queue, boolean autoAck, String tag, boolean noLocal, boolean exclusive, RecordConsumer consumer) {
        try {
            Consumer inner = consumer == null ? null : new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    consumer.onDelivery(new Record(body, envelope.getExchange(), envelope.getRoutingKey(), consumerTag, envelope.getDeliveryTag(), envelope.isRedeliver()));
                }
            };
            channel.basicConsume(queue, autoAck, tag, noLocal, exclusive, null, inner);
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * 默认手动回复，不接受本地消息，允许其他连接消费
     *
     * @param queue    队列名称
     * @param tag      消费者标签
     * @param consumer 消费者
     * @return 成功或失败
     */
    public boolean consume(String queue, String tag, RecordConsumer consumer) {
        return consume(queue, false, tag, true, false, consumer);
    }

    /**
     * 取消消费
     *
     * @param consumerTag 消费者标签
     * @return 成功或失败
     */
    public boolean cancel(String consumerTag) {
        try {
            channel.basicCancel(consumerTag);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 应答消息
     *
     * @param deliveryTag 投递标签
     * @param multiple    是否只应答本条消息
     * @return 成功或失败
     */
    public boolean ack(long deliveryTag, boolean multiple) {
        try {
            channel.basicAck(deliveryTag, multiple);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 拒绝一个或多个消息
     *
     * @param deliveryTag 投递标签
     * @param multiple    否只应答本条消息
     * @param requeue     是否加入队列（尽可能地保证在原来的位置）还是丢弃
     * @return 成功或失败
     */
    public boolean nack(long deliveryTag, boolean multiple, boolean requeue) {
        try {
            channel.basicNack(deliveryTag, multiple, requeue);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 拒绝一个消息
     *
     * @param deliveryTag 成功或失败
     * @param requeue     是否加入队列（尽可能地保证在原来的位置）还是丢弃
     * @return 成功或失败
     */
    public boolean reject(long deliveryTag, boolean requeue) {
        try {
            channel.basicReject(deliveryTag, requeue);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 让服务器重发未回复的消息
     *
     * @param requeue true发送到其他消费者，false发送到本消费者
     * @return 成功或失败
     */
    public boolean recover(boolean requeue) {
        try {
            channel.basicRecover(requeue);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 通道是否打开
     *
     * @return 是否打开
     */
    public boolean isOpen() {
        return channel.isOpen();
    }

    /**
     * 关闭通道和连接
     *
     * @return 成功或失败
     */
    public boolean close() {
        try {
            if (channel != null) {
                channel.removeReturnListener(this);
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
        if (returnConsumer != null) {
            returnConsumer.onReturn(new Return(replyCode, replyText, exchange, routingKey, body));
        }
    }

    @Override
    public void shutdownCompleted(ShutdownSignalException cause) {
        if (channel != null) {
            channel.removeReturnListener(this);
            channel.removeShutdownListener(this);
        }
    }
}
