package xxl.redis.lettuce;

import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionStateListener;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.event.Event;
import io.lettuce.core.event.command.CommandFailedEvent;
import io.lettuce.core.event.command.CommandListener;
import io.lettuce.core.event.command.CommandStartedEvent;
import io.lettuce.core.event.command.CommandSucceededEvent;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;

import java.util.Scanner;

/**
 * @author zhangliangbo
 * @since 2021/10/7
 **/


@Slf4j
public class LettuceMain {

    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://:selectdev@redis.dfs-dev.svc.comall.bj.public:6379/0");
        redisClient.getResources().eventBus().get().subscribe(new CoreSubscriber<Event>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                subscription.request(1);
            }

            @Override
            public void onNext(Event event) {
                log.info("EventBus onNext {}", event);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
        redisClient.addListener(new RedisConnectionStateListener() {
            @Override
            public void onRedisDisconnected(RedisChannelHandler<?, ?> connection) {
                log.info("onRedisDisconnected");
            }

            @Override
            public void onRedisExceptionCaught(RedisChannelHandler<?, ?> connection, Throwable cause) {
                log.info("onRedisExceptionCaught");
            }
        });
        redisClient.addListener(new CommandListener() {
            @Override
            public void commandStarted(CommandStartedEvent event) {
                log.info("commandStarted {}", event);
            }

            @Override
            public void commandSucceeded(CommandSucceededEvent event) {
                log.info("commandSucceeded {}", event);
            }

            @Override
            public void commandFailed(CommandFailedEvent event) {
                log.info("commandFailed {}", event);
            }
        });

        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.err.println("input:");
                String line = scanner.nextLine();
                if ("quit".equalsIgnoreCase(line)) {
                    break;
                }
                String response = connection.sync().set("lettuce", line);
                System.err.println("output:" + response);
            }
        } finally {
            redisClient.shutdown();
        }
    }

}
