package xxl.redis;

import com.google.gson.internal.$Gson$Preconditions;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字符串
 *
 * @author zhangliangbo
 * @since 2020/12/20
 **/

@Slf4j
public class RedisString {
    /**
     * 设置单个
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public static boolean set(String key, String value) {
        Jedis jedis = RedisSource.get().getResource();
        String res = jedis.set(key, value);
        jedis.close();
        return RedisContext.OK.equals(res);
    }

    /**
     * 获取单个
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        Jedis jedis = RedisSource.get().getResource();
        String res = jedis.get(key);
        jedis.close();
        return res;
    }


    /**
     * 设置多个
     *
     * @param kvs 键值对
     * @return 是否成功
     */
    public static boolean mSet(String... kvs) {
        Jedis jedis = RedisSource.get().getResource();
        String res = jedis.mset(kvs);
        jedis.close();
        return RedisContext.OK.equals(res);
    }

    /**
     * 获取多个
     *
     * @param keys 键
     * @return 值
     */
    public static List<String> mGet(String... keys) {
        Jedis jedis = RedisSource.get().getResource();
        List<String> res = jedis.mget(keys);
        jedis.close();
        return res;
    }

    /**
     * 获取多个
     *
     * @param keys 键
     * @return 值
     */
    public static List<String> pipelineGet(String... keys) {
        Jedis jedis = RedisSource.get().getResource();
        Pipeline pipeline = jedis.pipelined();
        Stream.of(keys).forEach(pipeline::get);
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
    public static boolean pipelineSet(String... kvs) {
        Jedis jedis = RedisSource.get().getResource();
        Pipeline pipeline = jedis.pipelined();
        io.vavr.collection.List.of(kvs)
                .sliding(2, 2)
                .forEach(strings -> pipeline.set(strings.get(0), strings.get(1)));
        List<String> res = pipeline.syncAndReturnAll().stream()
                .map(t -> (String) t)
                .collect(Collectors.toList());
        pipeline.close();
        jedis.close();
        return true;
    }

    /**
     * 匹配所有键
     *
     * @param patten 模式
     * @return 键列表
     */
    public static List<String> scan(String patten) {
        Jedis jedis = RedisSource.get().getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        List<String> res = new ArrayList<>();
        while (true) {
            ScanResult<String> scanResult = jedis.scan(cursor, new ScanParams().match(patten));
            res.addAll(scanResult.getResult());
            cursor = scanResult.getCursor();
            if (scanResult.isCompleteIteration()) {
                break;
            }
        }
        jedis.close();
        return res;
    }

    /**
     * 匹配所有键
     *
     * @param pattern 模式
     * @return 键列表
     */
    public static List<String> scan(String pattern, int count) {
        Jedis jedis = RedisSource.get().getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        List<String> res = new ArrayList<>();
        while (true) {
            ScanResult<String> scan = jedis.scan(cursor, new ScanParams().match(pattern));
            res.addAll(scan.getResult());
            cursor = scan.getCursor();
            if (scan.isCompleteIteration() || res.size() >= count) {
                break;
            }
        }
        jedis.close();
        return res;
    }

    /**
     * 匹配键
     *
     * @param patten 模式
     * @return 键集合
     */
    public static Set<String> keys(String patten) {
        Jedis jedis = RedisSource.get().getResource();
        Set<String> keys = jedis.keys(patten);
        jedis.close();
        return keys;
    }

    /**
     * 删除键
     *
     * @param keys 键集合
     * @return 删除个数
     */
    public static long delete(String... keys) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.del(keys);
        jedis.close();
        return Optional.ofNullable(res).orElse(0L);
    }

    public static Long expire(String key, int seconds) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.expire(key, seconds);
        jedis.close();
        return Optional.ofNullable(res).orElse(0L);
    }

    public static Long ttl(String key) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.ttl(key);
        jedis.close();
        return Optional.ofNullable(res).orElse(0L);
    }

    public static Long memory(String key) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.memoryUsage(key);
        jedis.close();
        return Optional.ofNullable(res).orElse(0L);
    }

}
