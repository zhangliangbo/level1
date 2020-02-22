package xxl.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 * paho mqtt客户端
 */
public class Paho {
  private IMqttClient client;
  private String url;
  private String clientId;
  private boolean file;

  public Paho(String url, String clientId, boolean file) {
    this.url = url;
    this.clientId = clientId;
    this.file = file;
  }

  /**
   * 打开客户端
   *
   * @return
   */
  public boolean open() {
    try {
      client = new MqttClient(url, clientId, file ? new MqttDefaultFilePersistence() : new MemoryPersistence());
      return true;
    } catch (MqttException e) {
      return false;
    }
  }

  /**
   * 连接服务器
   *
   * @param options
   * @return
   */
  public boolean connect(PahoOptions options) {
    try {
      IMqttToken token = client.connectWithResult(options);
      token.waitForCompletion();
      return true;
    } catch (MqttException e) {
      return false;
    }
  }

  /**
   * 断开连接
   *
   * @return
   */
  public boolean disconnect() {
    try {
      client.disconnect();
      return true;
    } catch (MqttException e) {
      return false;
    }
  }

  /**
   * 是否连接
   *
   * @return
   */
  public boolean isConnected() {
    return client.isConnected();
  }

  /**
   * 发布消息
   *
   * @param topic
   * @param body
   * @param qos
   * @param retained
   * @return
   */
  public int publish(String topic, byte[] body, int qos, boolean retained) {
    MqttTopic mqttTopic = client.getTopic(topic);
    try {
      MqttDeliveryToken token = mqttTopic.publish(body, qos, retained);
      token.waitForCompletion();
      return token.getMessageId();
    } catch (MqttException e) {
      return -1;
    }
  }

  /**
   * 订阅消息
   *
   * @param topic
   * @param qos
   * @param consumer
   * @return
   */
  public boolean subscribe(String topic, int qos, PahoConsumer consumer) {
    try {
      IMqttToken token = client.subscribeWithResponse(topic, qos, new IMqttMessageListener() {
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
          if (consumer != null) {
            consumer.onMessage(new PahoMessage(topic, message.getPayload(), message.getQos(), message.getId(), message.isRetained(), message.isDuplicate()));
          }
        }
      });
      token.waitForCompletion();
      return true;
    } catch (MqttException e) {
      return false;
    }
  }

  /**
   * 取消订阅
   *
   * @param topic
   * @return
   */
  public boolean unsubscribe(String topic) {
    try {
      client.unsubscribe(topic);
      return true;
    } catch (MqttException e) {
      return false;
    }
  }

  /**
   * 手动应答消息
   *
   * @param id
   * @param qos
   * @return
   */
  public boolean ack(int id, int qos) {
    try {
      client.messageArrivedComplete(id, qos);
      return true;
    } catch (MqttException e) {
      return false;
    }
  }

  /**
   * 设置手动应答
   *
   * @param manual
   */
  public void setManualAck(boolean manual) {
    client.setManualAcks(manual);
  }

  /**
   * 关闭客户端
   *
   * @return
   */
  public boolean close() {
    try {
      client.close();
      return true;
    } catch (MqttException e) {
      return false;
    }
  }
}
