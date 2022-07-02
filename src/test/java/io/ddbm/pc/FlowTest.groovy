package io.ddbm.pc

import io.ddbm.pc.exception.InterruptException
import io.ddbm.pc.simple.SimpleOrder
import org.junit.Test
import org.testng.Assert

class FlowTest extends BaseTest {

    @Test
    public void test1() {
        Flow flow = Flows.get("simple");

        flow.execute(new SimpleOrder(), null)
        flow.execute(new SimpleOrder(), "next")
        SimpleOrder s = new SimpleOrder();
        s.setStatus(null)
        flow.execute(s, "next")

        Assert.assertThrows(IllegalArgumentException.class, new Assert.ThrowingRunnable() {
            @Override
            void run() throws Throwable {
                flow.execute(null, "a")
            }
        })
        Assert.assertThrows(InterruptException.class, new Assert.ThrowingRunnable() {
            @Override
            void run() throws Throwable {
                flow.execute(new SimpleOrder(), "a")
            }
        })
    }
}
