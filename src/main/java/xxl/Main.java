package xxl;

import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.vavr.collection.List;

import java.util.function.Consumer;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        PublishProcessor<Integer> processor = PublishProcessor.create();
        processor.subscribe(new DisposableSubscriber<Integer>() {
            @Override
            protected void onStart() {
                request(1);
            }

            @Override
            public void onNext(Integer aLong) {
                System.out.println(Thread.currentThread().getName());
                System.err.println("get " + aLong);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println("request next");
                request(1);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.err.println("0-10" + Thread.currentThread().getName());
                List.range(0, 10)
                        .forEach(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) {
                                processor.onNext(integer);
                            }
                        });
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.err.println("10-20" + Thread.currentThread().getName());
                io.vavr.collection.List.range(10, 20)
                        .forEach(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) {
                                processor.onNext(integer);
                            }
                        });
            }
        }).start();
        Thread.sleep(1000000);
    }
}
