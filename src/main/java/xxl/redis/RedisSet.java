package xxl.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhangliangbo
 * @since 2021/4/23
 **/

@Slf4j
public class RedisSet {

    public static Long add(String key, String... values) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.sadd(key, values);
        jedis.close();
        return res;
    }

    public static Long card(String key) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.scard(key);
        jedis.close();
        return res;
    }

    public static List<String> members(String key) {
        Jedis jedis = RedisSource.get().getResource();
        Set<String> res = jedis.smembers(key);
        jedis.close();
        return new ArrayList<>(res);
    }

    public static List<String> random(String key, int count) {
        Jedis jedis = RedisSource.get().getResource();
        List<String> res = jedis.srandmember(key, count);
        jedis.close();
        return res;
    }

    public static Long remove(String key, String... values) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.srem(key, values);
        jedis.close();
        return res;
    }

    public static List<String> diff(String... keys) {
        Jedis jedis = RedisSource.get().getResource();
        Set<String> res = jedis.sdiff(keys);
        jedis.close();
        return new ArrayList<>(res);
    }

    public static List<String> union(String... keys) {
        Jedis jedis = RedisSource.get().getResource();
        Set<String> res = jedis.sunion(keys);
        jedis.close();
        return new ArrayList<>(res);
    }

    public static Long unionStore(String dstKey, String... keys) {
        Jedis jedis = RedisSource.get().getResource();
        Long res = jedis.sunionstore(dstKey, keys);
        jedis.close();
        return res;
    }

    public static List<String> inter(String... keys) {
        Jedis jedis = RedisSource.get().getResource();
        Set<String> res = jedis.sinter(keys);
        jedis.close();
        return new ArrayList<>(res);
    }

    public static List<String> scan(String key, String pattern) {
        Jedis jedis = RedisSource.get().getResource();
        List<String> res = new ArrayList<>();
        String cursor = ScanParams.SCAN_POINTER_START;
        while (true) {
            ScanResult<String> sscan = jedis.sscan(key, cursor, new ScanParams().match(pattern));
            res.addAll(sscan.getResult());
            cursor = sscan.getCursor();
            if (sscan.isCompleteIteration()) {
                break;
            }
        }
        jedis.close();
        return new ArrayList<>(res);
    }

    public static List<String> scan(String key, String pattern, int count) {
        Jedis jedis = RedisSource.get().getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        List<String> res = new ArrayList<>();
        while (true) {
            ScanResult<String> sscan = jedis.sscan(key, cursor, new ScanParams().match(pattern));
            res.addAll(sscan.getResult());
            cursor = sscan.getCursor();
            if (sscan.isCompleteIteration() || res.size() >= count) {
                break;
            }
        }
        jedis.close();
        return res;
    }

}
