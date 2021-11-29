package xxl.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangliangbo
 * @since 2021/11/27
 **/


@Slf4j
public class RateLimiter {

    private volatile StatefulRedisConnection<String, String> connection;
    private volatile String scriptSha1;
    private String uri;
    private int replenishRate;
    private int burstCapacity;
    private int requestedTokens;

    public RateLimiter(String uri, int replenishRate, int burstCapacity, int requestedTokens) {
        this.uri = uri;
        this.replenishRate = replenishRate;
        this.burstCapacity = burstCapacity;
        this.requestedTokens = requestedTokens;
        getConnection();
        getScriptSha1();
    }

    public boolean acquire(String id) {
        // How many requests per second do you want a user to be allowed to do?
        int replenishRate = this.replenishRate;

        // How much bursting do you want to allow?
        int burstCapacity = this.burstCapacity;

        // How many tokens are requested per request?
        int requestedTokens = this.requestedTokens;

        List<String> keys = getKeys(id);
        // The arguments to the LUA script. time() returns unixtime in seconds.
        List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "",
                Instant.now().getEpochSecond() + "", requestedTokens + "");

        // allowed, tokens_left = redis.eval(SCRIPT, keys, args)
        List<Long> result = execute(keys, scriptArgs);
        return result.size() > 0 && result.get(0) == 1L;
    }

    private List<String> getKeys(String id) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "request_rate_limiter.{" + id;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    private List<Long> execute(List<String> keys, List<String> scriptArgs) {
        try {
            if (StringUtils.isEmpty(getScriptSha1())) {
                return Arrays.asList(1L, -1L);
            }
            return getConnection().sync().evalsha(getScriptSha1(), ScriptOutputType.MULTI, keys.toArray(new String[0]), scriptArgs.toArray(new String[0]));
        } catch (Exception e) {
            log.info("请求限流信息报错", e);
            return Arrays.asList(1L, -1L);
        }
    }

    /**
     * DCL
     */
    private StatefulRedisConnection<String, String> getConnection() {
        StatefulRedisConnection<String, String> connection = this.connection;
        if (Objects.isNull(connection)) {
            synchronized (this) {
                connection = this.connection;
                if (Objects.isNull(connection)) {
                    RedisClient redisClient = RedisClient.create(this.uri);
                    connection = redisClient.connect();
                    this.connection = connection;
                }
            }
        }
        return connection;
    }

    private String getScriptSha1() {
        String sha1 = this.scriptSha1;
        if (Objects.isNull(sha1)) {
            synchronized (this) {
                sha1 = this.scriptSha1;
                if (Objects.isNull(sha1)) {
                    sha1 = doLoadScript();
                    this.scriptSha1 = sha1;
                }
            }
        }
        return sha1;
    }

    private String doLoadScript() {
        try (InputStream inputStream = getClass().getResourceAsStream("/request_rate_limiter.lua")) {
            if (Objects.isNull(inputStream)) {
                return "";
            }
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                int len;
                byte[] buffer = new byte[256];
                while (true) {
                    len = inputStream.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    baos.write(buffer, 0, len);
                }
                byte[] script = baos.toByteArray();
                try {
                    return getConnection().sync().scriptLoad(script);
                } catch (Exception e) {
                    log.info("doLoadScript报错", e);
                    return "";
                }
            }
        } catch (Exception e) {
            log.info("doLoadScript报错", e);
            return "";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int count = 0;
        RateLimiter rateLimiter = new RateLimiter("redis://:civic@localhost/10", 1, 50, 1);
        while (true) {
            boolean elapsedSecond = rateLimiter.acquire("xxx");
            if (elapsedSecond) {
                log.info("{} 获取 {}", Thread.currentThread(), System.nanoTime());
                ++count;
                if (count > 100) {
                    Thread.sleep(60000);
                    count = 0;
                }
            }
        }
    }
}
