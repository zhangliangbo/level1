package xxl.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import xxl.mathematica.string.StringRiffle;

import java.util.Arrays;
import java.util.Properties;

public class KfkStream {
  /**
   * 通用设置
   *
   * @param servers
   * @param id
   * @return
   */
  public static Properties props(String[] servers, String id) {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, id);
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, StringRiffle.stringRiffle(Arrays.asList(servers), ","));
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    return props;
  }
}
