package xxl.redis

import org.apache.commons.lang3.time.StopWatch
import redis.clients.jedis.Jedis
import redis.clients.jedis.Pipeline
import redis.clients.jedis.Transaction

import java.util.stream.Collectors
import java.util.stream.IntStream

class RedisTest extends GroovyTestCase {

    @Override
    void setUp() throws Exception {
        RedisSource.use("redis://localhost:6379/0", "civic")
    }

    void testRandomPort() {
        ServerSocket serverSocket = new ServerSocket(0, 1, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}))
        println(serverSocket.getLocalPort())
    }

    @Override
    void tearDown() throws Exception {
        super.tearDown()
        RedisSource.close()
    }

    void testScan() {
        String patten = "pipeline*"
        List<String> res = RedisString.scan(patten)
        System.err.println(res.size())
//        RedisString.delete(res.toArray(new String[]{}))
    }

    void testKeys() {
        Jedis jedis = RedisSource.get().getResource()
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
        Jedis jedis = RedisSource.get().getResource()
        for (int i = 0; i < 10000; i++) {
            jedis.set("pipeline" + i, "value" + i)
        }
        long time = System.currentTimeMillis() - s;
        System.err.println("10000个命令【单条发送】总耗时：" + time)
        jedis.close()
    }

    void testPipeline() {
        long s = System.currentTimeMillis()
        Jedis jedis = RedisSource.get().getResource()
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
        Jedis jedis = RedisSource.get().getResource()
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
        Jedis jedis = RedisSource.get().getResource()
        Transaction transaction = jedis.multi()
        for (int i = 0; i < 100; i++) {
            transaction.set("transaction" + i, "transaction" + i)
        }
        String res = transaction.discard()
        System.err.println(res)
        jedis.close()
    }

    void testWatch() {
        Jedis jedis = RedisSource.get().getResource()
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
        Jedis jedis = RedisSource.get().getResource()
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

    void testString() {
        def keys = IntStream.range(0, 10000)
                .mapToObj({ x -> String.valueOf(x) })
                .collect(Collectors.toList())
        StopWatch stopWatch = new StopWatch()
        stopWatch.start()
        def res = RedisString.mGet(keys as String[])
        stopWatch.split()
        println(stopWatch.toSplitString())
        println(res.size())

        stopWatch.reset()
        stopWatch.start()
        res = keys.stream()
                .map({ t -> RedisString.get(t) })
                .collect(Collectors.toList())
        stopWatch.split()
        println(stopWatch.toSplitString())
        println(res.size())

        stopWatch.reset()
        stopWatch.start()
        res = RedisString.pipelineGet(keys as String[])
        stopWatch.split()
        println(stopWatch.toSplitString())
        println(res.size())

    }

    void testHash() {
        def key = "civic"
        def keys = IntStream.range(0, 50000)
                .mapToObj({ x -> String.valueOf(x) })
                .collect(Collectors.toList())
        StopWatch stopWatch = new StopWatch()
        stopWatch.start()
        def res = RedisHash.mGet(key, keys as String[])
        stopWatch.split()
        println(stopWatch.toSplitString())
        println(res.size())

        stopWatch.reset()
        stopWatch.start()
        res = keys.stream()
                .map({ t -> RedisHash.get(key, t) })
                .collect(Collectors.toList())
        stopWatch.split()
        println(stopWatch.toSplitString())
        println(res.size())

        stopWatch.reset()
        stopWatch.start()
        res = RedisHash.pipelineGet(key, keys as String[])
        stopWatch.split()
        println(stopWatch.toSplitString())
        println(res.size())

    }

}
