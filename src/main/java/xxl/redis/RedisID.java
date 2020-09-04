package xxl.redis;

import redis.clients.jedis.Jedis;

/**
 * redis自增ID
 */
public class RedisID {
  /**
   * 自增长整型
   *
   * @param key
   * @return
   */
  public static Long nextLong(String key) {
    Jedis jedis = RedisSource.get().getResource();
    Long res = jedis.incr(key);
    jedis.close();
    return res;
  }
}
