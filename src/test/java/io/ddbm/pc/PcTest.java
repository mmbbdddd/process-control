package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.notify.AsyncResultNotify;
import io.ddbm.pc.simple.SimpleOrder;
import io.ddbm.pc.utils.TimeoutWatch;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class PcTest extends BaseTest {

    @Test
    public void sync() {
        SimpleOrder s = new SimpleOrder(Math.random());
        try {
            pc.sync("simple", s, null, new TimeoutWatch(2, TimeUnit.MILLISECONDS));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    public void async() {
        SimpleOrder s = new SimpleOrder(Math.random());
        try {
            AsyncResultNotify notify = new AsyncResultNotify();
            pc.async("simple", s, null);
            Thread.sleep(1000l);
            System.out.println(s.getStatus());
        } catch (InterruptException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void chaos() {
        SimpleOrder s = new SimpleOrder(Math.random());
        try {
            pc.chaos("simple", s, null);
        } catch (InterruptException e) {
            e.printStackTrace();
        }
    }

}