package xxl.reactor;

import io.vavr.collection.List;
import reactor.core.publisher.EmitterProcessor;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class ReactorDemo {
  public static void main(String[] args) {
    CountDownLatch latch = new CountDownLatch(1);
    EmitterProcessor<Integer> processor = EmitterProcessor.create(16);
    processor.subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) {
        System.err.println("all: " + integer);
      }
    });
    List.range(0, 10).forEach(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            System.out.println("next: " + integer);
            processor.onNext(integer);
          }
        }).start();
        if (integer == 5) {
          processor.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
              System.err.println("5: " + integer);
            }
          });
        }
      }
    });
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
