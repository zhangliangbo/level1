package xxl.redis;

import io.vavr.Lazy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.Pool;

import java.net.URI;

/**
 * Redis源
 * 整个redis模块都使用这个redis源
 *
 * @author zhangliangbo
 * @time 2020/8/31
 */
public class RedisSource {
    private static Lazy<Pool<Jedis>> jedisPool;

    /**
     * 设置redis源
     *
     * @param uri 资源路径
     */
    public static void use(String uri) {
        if (jedisPool != null) {
            jedisPool.get().close();
        }
        jedisPool = Lazy.of(() -> new JedisPool(URI.create(uri)));
    }

    /**
     * 关闭redis源
     */
    public static void close() {
        if (jedisPool == null) {
            return;
        }
        jedisPool.get().close();
    }

    /**
     * 获取redis源
     *
     * @return redis连接池
     */
    protected static Pool<Jedis> get() {
        if (jedisPool == null) {
            throw new IllegalStateException("使用RedisSource.use(..)设置Redis源");
        }
        return jedisPool.get();
    }

}
