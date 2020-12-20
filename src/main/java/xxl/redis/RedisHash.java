package xxl.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 哈希表
 *
 * @author zhangliangbo
 * @since 2020/12/20
 **/

@Slf4j
public class RedisHash {
    /**
     * 设置单个
     *
     * @param key       键
     * @param hashKey   哈希键
     * @param hashValue 哈希值
     * @return 是否成功
     */
    public static Long set(String key, String hashKey, String hashValue) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.hset(key, hashKey, hashValue);
        jedis.close();
        return res;
    }

    /**
     * 获取单个
     *
     * @param key     键
     * @param hashKey 哈希键
     * @return 值
     */
    public static String get(String key, String hashKey) {
        Jedis jedis = RedisSource.get().getResource();
        String res = jedis.hget(key, hashKey);
        jedis.close();
        return res;
    }


    /**
     * 设置多个
     *
     * @param key 键
     * @param kvs 哈希键值对
     * @return 是否成功
     */
    public static boolean mSet(String key, Map<String, String> kvs) {
        Jedis jedis = RedisSource.get().getResource();
        String res = jedis.hmset(key, kvs);
        jedis.close();
        return RedisContext.OK.equals(res);
    }

    /**
     * 获取多个
     *
     * @param key      键
     * @param hashKeys 哈希键
     * @return 值
     */
    public static List<String> mGet(String key, String... hashKeys) {
        Jedis jedis = RedisSource.get().getResource();
        List<String> res = jedis.hmget(key, hashKeys);
        jedis.close();
        return res;
    }

    /**
     * 获取多个
     *
     * @param key      键
     * @param hashKeys 哈希键
     * @return 值
     */
    public static List<String> pipelineGet(String key, String... hashKeys) {
        Jedis jedis = RedisSource.get().getResource();
        Pipeline pipeline = jedis.pipelined();
        Stream.of(hashKeys).forEach(t -> pipeline.hget(key, t));
        List<String> res = pipeline.syncAndReturnAll()
                .stream()
                .map(t -> (String) t)
                .collect(Collectors.toList());
        jedis.close();
        return res;
    }

    /**
     * 获取多个
     *
     * @param kvs 键
     * @return 值
     */
    public static boolean pipelineSet(String key, Map<String, String> kvs) {
        Jedis jedis = RedisSource.get().getResource();
        Pipeline pipeline = jedis.pipelined();
        kvs.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                pipeline.hset(key, s, s2);
            }
        });
        List<Long> res = pipeline.syncAndReturnAll().stream()
                .map(t -> (Long) t)
                .collect(Collectors.toList());
        jedis.close();
        return true;
    }

}
