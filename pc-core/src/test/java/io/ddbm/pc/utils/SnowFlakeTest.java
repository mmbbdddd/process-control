package io.ddbm.pc.utils;

import org.junit.Test;


public class SnowFlakeTest {

    @Test
    public void testNextId() {
        SnowFlake snowFlake = new SnowFlake(2, 6);

        for (int i = 0; i < (1 << 12); i++) {
            System.out.println(snowFlake.nextId());
        }
    }
}