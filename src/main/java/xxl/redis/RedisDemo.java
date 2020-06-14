package xxl.redis;

import redis.clients.jedis.Jedis;

public class RedisDemo {
  public static void main(String[] args) {
    Jedis jedis = new Jedis("redis", 6379);
    jedis.connect();
    if (jedis.isConnected()) {
      System.err.println("connected.");
    } else {
      System.err.println("not connected.");
    }
  }
}
