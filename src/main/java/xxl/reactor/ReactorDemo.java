package xxl.reactor;

import io.vavr.collection.List;
import reactor.extra.processor.TopicProcessor;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class ReactorDemo {
  public static void main(String[] args) {
    CountDownLatch latch = new CountDownLatch(1);
    TopicProcessor<Integer> processor = TopicProcessor.create("zlb", 16);
    processor.subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) {
        System.err.println("all: " + integer);
      }
    });
    List.range(0, 10).forEach(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) {
        if (integer == 5) {
          processor.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
              System.err.println("5: " + integer);
            }
          });
        }
        new Thread(new Runnable() {
          @Override
          public void run() {
            processor.onNext(integer);
          }
        }).start();
      }
    });
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
