package io.ddbm.pc.utils;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TimeoutWatchTest {

    @Test
    public void timeout() throws InterruptedException {
        TimeoutWatch tw = new TimeoutWatch(2, TimeUnit.SECONDS);
        Thread.sleep(10);
        System.out.println(tw.costTime());
        Thread.sleep(1000);
        System.out.println(tw.costUnit());
//        Thread.sleep(2000);
        System.out.println(tw.isTimeout());
    }
}