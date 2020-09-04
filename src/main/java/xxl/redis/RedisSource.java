package xxl.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

/**
 * Redis源
 * 整个redis模块都使用这个redis源
 *
 * @author zhangliangbo
 * @time 2020/8/31
 */
public class RedisSource {
  private static Pool<Jedis> jedisPool;

  /**
   * 设置redis源
   *
   * @param pool
   */
  public static void use(Pool<Jedis> pool) {
    jedisPool = pool;
  }

  /**
   * 获取redis源
   *
   * @return
   */
  protected static Pool<Jedis> get() {
    if (jedisPool == null) {
      throw new IllegalStateException("使用RedisSource.use(..)设置Redis源");
    }
    return jedisPool;
  }
}
