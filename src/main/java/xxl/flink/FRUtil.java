package xxl.flink;

import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;

class FRUtil {
    static RMQConnectionConfig config(String host, Integer port, String username, String password, String vHost) {
        return new RMQConnectionConfig.Builder()
                .setHost(host)
                .setPort(port)
                .setUserName(username)
                .setPassword(password)
                .setVirtualHost(vHost)
                .setAutomaticRecovery(true)
                .setTopologyRecoveryEnabled(true)
                .build();
    }
}
