package xxl.reactor;

import io.vavr.collection.List;
import reactor.extra.processor.TopicProcessor;
import reactor.extra.processor.WorkQueueProcessor;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class ReactorDemo {
  public static void main(String[] args) {
    int threadCount = 10;
    CountDownLatch latch = new CountDownLatch(threadCount);
    java.util.List<Integer> all = new CopyOnWriteArrayList<>();
    java.util.List<Integer> three = new CopyOnWriteArrayList<>();
    java.util.List<Integer> seven = new CopyOnWriteArrayList<>();
    WorkQueueProcessor<Integer> processor = WorkQueueProcessor.create("xxl", 16);
    processor.subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) {
        all.add(integer);
      }
    });
    List.range(0, threadCount).forEach(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) {
        if (integer == 3) {
          processor.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
              three.add(integer);
            }
          });
        } else if (integer == 7) {
          processor.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
              seven.add(integer);
            }
          });
        }
        new Thread(new Runnable() {
          @Override
          public void run() {
            processor.onNext(integer);
            latch.countDown();
          }
        }).start();
      }
    });
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.err.println(List.ofAll(all).mkString(","));
    System.err.println(List.ofAll(three).mkString(","));
    System.err.println(List.ofAll(seven).mkString(","));
  }
}
