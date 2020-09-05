package xxl.redis


import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.Pipeline
import redis.clients.jedis.Transaction
import redis.clients.jedis.util.Pool

class RedisIDTest extends GroovyTestCase {

    @Override
    void setUp() throws Exception {
        RedisSource.use("redis://:123456@localhost:6379/0")
    }

    @Override
    void tearDown() throws Exception {
        super.tearDown()
        RedisSource.close()
    }

    void testKeys() {
        Jedis jedis = pool.getResource()
        Set<String> keys = jedis.keys("pipeline*")
        System.err.println(keys)
        Pipeline pipeline = jedis.pipelined()
        for (String k : keys) {
            pipeline.del(k)
        }
        pipeline.syncAndReturnAll()
        jedis.close()
    }

    void testNormal() {
        long s = System.currentTimeMillis()
        Jedis jedis = pool.getResource()
        for (int i = 0; i < 10000; i++) {
            jedis.set("pipeline" + i, "value" + i)
        }
        long time = System.currentTimeMillis() - s;
        System.err.println("10000个命令【单条发送】总耗时：" + time)
        jedis.close()
    }

    void testPipeline() {
        long s = System.currentTimeMillis()
        Jedis jedis = pool.getResource()
        Pipeline pipeline = jedis.pipelined()
        for (int i = 0; i < 10000; i++) {
            pipeline.set("pipeline" + i, "value" + i)
        }
        pipeline.syncAndReturnAll()
        long time = System.currentTimeMillis() - s
        System.err.println("10000个命令【批量发送】总耗时：" + time)
        jedis.close()
    }

    void testPipelineTrans() {
        Jedis jedis = pool.getResource()
        Pipeline pipeline = jedis.pipelined()
        pipeline.multi()
        for (int i = 0; i < 10000; i++) {
            pipeline.set("pipeline" + i, "value" + i)
        }
        pipeline.discard()
        List<Object> res = pipeline.syncAndReturnAll()
        System.err.println(res)
        jedis.close()
    }

    void testTransaction() {
        Jedis jedis = pool.getResource()
        Transaction transaction = jedis.multi()
        for (int i = 0; i < 100; i++) {
            transaction.set("transaction" + i, "transaction" + i)
        }
        String res = transaction.discard()
        System.err.println(res)
        jedis.close()
    }

    void testWatch() {
        Jedis jedis = pool.getResource()
        while (true) {
            jedis.watch("zlb")
            Transaction transaction = jedis.multi()
            transaction.incr("zlb")
            List<Object> res = transaction.exec()
            if (res != null) {
                System.err.println(res)
                break
            }
        }
        jedis.close()
    }

    void testNoWatch() {
        Jedis jedis = pool.getResource()
        Long res = jedis.incr("zlb")
        System.err.println(res)
        jedis.close()
    }

    void testMany() {
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                void run() {
                    System.err.println(RedisID.nextLong("zlb"))
                }
            }).start()
        }
        Thread.sleep(3000)
    }

    void testCas() {
        while (true) {
            def expect = RedisCas.get("zlb")
            println(expect)
            def update = String.valueOf(Integer.valueOf(expect) + 1)
            def res = RedisCas.redisCas("zlb", expect, update)
            println(res)
            if (res) break
        }
    }

    void testNextLong() {
        println(RedisID.nextLong("zlb"))
    }
}
