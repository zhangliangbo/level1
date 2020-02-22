package xxl.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class PahoOptions extends MqttConnectOptions {
  public PahoOptions(String username, String password, boolean autoReconnect, boolean cleanSession) {
    setUserName(username);
    if (password != null) {
      setPassword(password.toCharArray());
    }
    setAutomaticReconnect(autoReconnect);
    setCleanSession(cleanSession);
  }
}
