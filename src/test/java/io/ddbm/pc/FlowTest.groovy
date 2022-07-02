package io.ddbm.pc

import io.ddbm.pc.exception.InterruptException
import io.ddbm.pc.simple.SimpleOrder
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.testng.Assert

class FlowTest extends BaseTest {


    @Test
    public void dumpHead() {
        Flow flow = Flows.get("simple");

        SimpleOrder s = new SimpleOrder(Math.random());
        flow.execute(s, "next")
        flow.execute(s, "next")
        flow.execute(s, "next")
    }

    @Test
    public void pc() {
        SimpleOrder s = new SimpleOrder(Math.random());
        pc.chaos("simple", s, null)
    }

    @Test
    public void in_outTest() {
        Flow flow = Flows.get("simple");

        Assert.assertThrows(IllegalArgumentException.class, new Assert.ThrowingRunnable() {
            @Override
            void run() throws Throwable {
                flow.execute(null, "a")
            }
        })
        Assert.assertThrows(InterruptException.class, new Assert.ThrowingRunnable() {
            @Override
            void run() throws Throwable {
                flow.execute(new SimpleOrder(Math.random()), "a")
            }
        })
    }
}
