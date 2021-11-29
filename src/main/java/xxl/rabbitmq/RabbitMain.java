package xxl.rabbitmq;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.MicrometerMetricsCollector;
import com.rabbitmq.client.impl.nio.NioParams;
import io.micrometer.core.instrument.Clock;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author zhangliangbo
 * @since 2021/10/12
 **/

@Slf4j
public class RabbitMain {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("mqtt");
        connectionFactory.setPassword("mqtt");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setThreadFactory(new DefaultThreadFactory("civic"));
        connectionFactory.useNio();
        connectionFactory.setMetricsCollector(new MicrometerMetricsCollector(new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM)));
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                print("handleAck", deliveryTag);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                print("handleNack", deliveryTag);
            }
        });
        String queue = channel.queueDeclare().getQueue();
        channel.basicConsume(queue, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("handleDelivery {} {} {}", consumerTag, envelope.getDeliveryTag(), new String(body));
            }
        });
        channel.confirmSelect();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.err.println("input:");
                String line = scanner.nextLine();
                if ("quit".equals(line)) {
                    break;
                }
                channel.basicPublish("", queue, true, null, line.getBytes());
                if (channel.waitForConfirms()) {
                    print("waitForConfirms", "发送成功");
                } else {
                    print("waitForConfirms", "发送失败");
                }
            }
        } catch (InterruptedException e) {
            log.info("报错", e);
        } finally {
            channel.close();
            connection.close();
        }
    }

    private static void print(String tag, Object object) {
        log.info("{} {}", tag, object);
    }
}
