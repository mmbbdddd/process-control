package io.ddbm.pc;

import org.junit.Test;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PcTest {

    @Test
    public void sync() {

        ReentrantLock lock = new ReentrantLock();
        Condition     c    = lock.newCondition();
        try {
            Thread.sleep(2000);
            c.await(1000, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            Assert.isTrue(true);
        }

    }

}