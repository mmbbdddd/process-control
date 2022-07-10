package io.ddbm.pc.utils

import ognl.Ognl
import org.junit.Test

class OnglTest {
    @Test
    public void test() {
        System.out.println(Ognl.getValue("1 +1", null))
    }
}
