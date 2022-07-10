package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.simple.SimpleOrder;
import io.ddbm.pc.utils.TimeoutWatch;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class PcTest extends BaseTest {

    @Test
    public void sync() {
        SimpleOrder s = new SimpleOrder(Math.random());
        try {
            pc.sync("simple", s, null, new TimeoutWatch(1000, TimeUnit.MILLISECONDS));
        } catch (Throwable e) {
//            e.printStackTrace();
        }
    }

    @Test
    public void async() {
        SimpleOrder s = new SimpleOrder(Math.random());
        try {
            pc.async("simple", s, null);
        } catch (InterruptException e) {

        }
    }

    @Test
    public void chaos() {
        SimpleOrder s = new SimpleOrder(Math.random());
        try {
            pc.chaos("simple", s, null);
        } catch (InterruptException e) {
//            e.printStackTrace();
        }
    }

}