package xxl;

import org.apache.commons.lang3.time.StopWatch;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(1000);
        watch.split();
        System.err.println(watch.formatSplitTime());
        Thread.sleep(1000);
        watch.split();
        System.err.println(watch.formatSplitTime());
        Thread.sleep(1000);
        watch.split();
        System.err.println(watch.formatSplitTime());
        watch.stop();
    }
}
