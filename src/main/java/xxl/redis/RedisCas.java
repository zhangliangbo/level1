package xxl.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * redis cas(compare and swap)
 *
 * @author zhangliangbo
 * @time 2020/8/31
 */
public class RedisCas {
  /**
   * 锁定旧值，并尝试替换成新值
   * 如果旧值在提交事务之前没有被修改，则更新事务成功；否则，更新事务失败
   * @param key
   * @param update
   * @return
   */
  public static boolean redisCas(String key, String update) {
    Jedis jedis = RedisSource.get().getResource();
    if (!RedisContext.OK.equals(jedis.watch(key))) {
      return false;
    }
    Transaction transaction = jedis.multi();
    transaction.set(key, update);
    List<Object> objects = transaction.exec();
    if (objects == null || objects.size() == 0) {
      return false;
    }
    boolean res = objects.get(0).equals(RedisContext.OK);
    jedis.close();
    return res;
  }
}
