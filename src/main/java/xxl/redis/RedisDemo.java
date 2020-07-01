package xxl.redis;

import redis.clients.jedis.Jedis;

import java.util.function.Consumer;

public class RedisDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis", 6379);
        jedis.connect();
        if (jedis.isConnected()) {
            System.err.println("connected.");
        } else {
            System.err.println("not connected.");
        }
        System.err.println("flushall: " + jedis.flushAll());
        long start = System.currentTimeMillis();
        io.vavr.collection.List.range(0, 10)
                .forEach(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        System.err.println(jedis.set(String.valueOf(integer), String.valueOf(integer)));
                    }
                });
        System.err.println("time: " + (System.currentTimeMillis() - start));
        System.err.println("bgsave: " + jedis.bgsave());
    }
}
