package xxl.redis;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public static boolean mSet(String key, String... kvs) {
        Jedis jedis = RedisSource.get().getResource();
        Map<String, String> map = io.vavr.collection.List.of(kvs)
                .sliding(2, 2)
                .toJavaMap(strings -> Tuple.of(strings.get(0), strings.get(1)));
        String res = jedis.hmset(key, map);
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
        pipeline.close();
        jedis.close();
        return res;
    }

    /**
     * 获取多个
     *
     * @param kvs 键
     * @return 值
     */
    public static boolean pipelineSet(String key, String... kvs) {
        Jedis jedis = RedisSource.get().getResource();
        Pipeline pipeline = jedis.pipelined();
        io.vavr.collection.List.of(kvs)
                .sliding(2, 2)
                .forEach(strings -> pipeline.hset(key, strings.get(0), strings.get(1)));
        List<Long> res = pipeline.syncAndReturnAll().stream()
                .map(t -> (Long) t)
                .collect(Collectors.toList());
        pipeline.close();
        jedis.close();
        return true;
    }

    /**
     * 获取所有的键
     *
     * @param key 键
     * @return 值
     */
    public static List<String> keys(String key) {
        Jedis jedis = RedisSource.get().getResource();
        Set<String> hkeys = jedis.hkeys(key);
        jedis.close();
        return new ArrayList<>(hkeys);
    }

    public static Long del(String key, String... hashKeys) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.hdel(key, hashKeys);
        jedis.close();
        return res;
    }

    /**
     * 获取所有的键
     *
     * @param key 键
     * @return 值
     */
    public static Long len(String key) {
        Jedis jedis = RedisSource.get().getResource();
        Long len = jedis.hlen(key);
        jedis.close();
        return len;
    }

    public static List<String> scan(String key, String pattern) {
        Jedis jedis = RedisSource.get().getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        List<String> res = new ArrayList<>();
        while (true) {
            ScanResult<Map.Entry<String, String>> hscan = jedis.hscan(key, cursor, new ScanParams().match(pattern));
            res.addAll(hscan.getResult().stream().map(Map.Entry::getKey).collect(Collectors.toList()));
            cursor = hscan.getCursor();
            if (hscan.isCompleteIteration()) {
                break;
            }
        }
        jedis.close();
        return res;
    }

    public static List<String> scan(String key, String pattern, int count) {
        Jedis jedis = RedisSource.get().getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        ScanResult<Map.Entry<String, String>> hscan = jedis.hscan(key, cursor, new ScanParams().match(pattern).count(count));
        jedis.close();
        return hscan.getResult().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

}
