package xxl.redis;

import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * List操作
 *
 * @author zhangliangbo
 * @since 2021/5/17
 **/


@Slf4j
public class RedisList {

    public static Long lPush(String key, String... values) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.lpush(key, values);
        jedis.close();
        return res;
    }

    public static List<String> lPop(String key, int count) {
        Jedis jedis = RedisSource.get().getResource();
        List<String> res = jedis.lpop(key, count);
        jedis.close();
        return res;
    }

    public static Long rPush(String key, String... values) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.rpush(key, values);
        jedis.close();
        return res;
    }

    public static List<String> rPop(String key, int count) {
        Jedis jedis = RedisSource.get().getResource();
        List<String> res = jedis.rpop(key, count);
        jedis.close();
        return res;
    }

    public static List<String> range(String key, long start, long stop) {
        Jedis jedis = RedisSource.get().getResource();
        List<String> res = jedis.lrange(key, start, stop);
        jedis.close();
        return res;
    }

    public static Long remove(String key, long count, String value) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.lrem(key, count, value);
        jedis.close();
        return res;
    }

    public static Long len(String key) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.llen(key);
        jedis.close();
        return res;
    }

    public static String index(String key, long index) {
        Jedis jedis = RedisSource.get().getResource();
        String res = jedis.lindex(key, index);
        jedis.close();
        return res;
    }

}
