package io.ddbm.pc

import io.ddbm.pc.simple.SimpleOrder
import org.junit.Test

public class SimpleTest extends BaseTest {

    @Test
    public void test1() {
        Flow flow = Flows.get("simple");

        flow.execute(new SimpleOrder(), "a")
    }
}
