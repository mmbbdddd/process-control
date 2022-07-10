package io.ddbm.pc

import io.ddbm.pc.simple.SimpleOrder
import org.junit.Test

public class SimpleTest extends BaseTest {

    @Test
    public void test1() {
        pc.chaos("simple", new SimpleOrder(), null)
    }

    @Test
    public void tt() {
        for (; ;) {
            new SimpleOrder()
        }
    }
}
