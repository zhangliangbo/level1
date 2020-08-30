package xxl.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.util.Pool;

import java.util.List;

/**
 * redis自增ID
 */
public class RedisID {
  /**
   * 自增长整型
   *
   * @param pool
   * @param key
   * @return
   */
  public static Long nextLong(Pool<Jedis> pool, String key) {
    Jedis jedis = pool.getResource();
    Long res = null;
    while (true) {
      jedis.watch(key);
      Transaction transaction = jedis.multi();
      transaction.incr(key);
      List<Object> list = transaction.exec();
      if (list != null) {
        if (list.size() > 0) {
          res = (Long) list.get(0);
        }
        break;
      }
    }
    jedis.close();
    return res;
  }
}
