package xxl.redis;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.Tuple5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.JedisURIHelper;
import redis.clients.jedis.util.Pool;
import xxl.source.SshSource;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

/**
 * Redis源
 * 整个redis模块都使用这个redis源
 *
 * @author zhangliangbo
 * @time 2020/8/31
 */
@Slf4j
public class RedisSource {

    private static Lazy<Pool<Jedis>> jedisPool;

    private static SshSource sshSource = new SshSource(55556);


    public static void use(String url) {
        use(url, null);
    }

    /**
     * 设置redis源
     *
     * @param url      资源路径
     * @param password 密码
     */
    public static void use(String url, String password) {
        if (jedisPool != null && jedisPool.get() != null) {
            jedisPool.get().close();
        }
        URI uri = URI.create(url);
        jedisPool = Lazy.of(() -> new JedisPool(new GenericObjectPoolConfig<>(),
                uri.getHost(), uri.getPort(), Protocol.DEFAULT_TIMEOUT,
                password, JedisURIHelper.getDBIndex(uri)));
    }

    /**
     * 设置redis源
     *
     * @param url      资源路径
     * @param password 密码
     * @param sshHost  跳板主机
     * @param sshPort  跳板端口
     * @param sshUser  跳板用户
     * @param sshPwd   跳板密码
     */
    public static void use(String url, String password, String sshHost, Integer sshPort, String sshUser, String sshPwd) {
        String redisPrefix = "redis:";
        URI uri = URI.create(url);
        if (!url.startsWith(redisPrefix)) {
            throw new IllegalArgumentException("url must start with " + redisPrefix);
        }
        String finalUrl = sshSource.connectForUri(sshHost, sshPort, sshUser, sshPwd, uri, url);
        use(finalUrl, password);
    }

    /**
     * 设置redis源
     *
     * @param masterName 主服务器名称
     * @param sentinels  哨兵地址
     */
    public static void useSentinel(String masterName, String... sentinels) {
        if (jedisPool != null && jedisPool.get() != null) {
            jedisPool.get().close();
        }
        jedisPool = Lazy.of(() -> new JedisSentinelPool(masterName, new HashSet<>(Arrays.asList(sentinels))));
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
