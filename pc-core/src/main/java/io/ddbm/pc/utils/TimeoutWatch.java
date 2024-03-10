package io.ddbm.pc.utils;

import java.util.concurrent.TimeUnit;


public class TimeoutWatch {
    private final Integer timeout;

    private final TimeUnit unit;

    private final long start;

    public TimeoutWatch(Integer timeout, TimeUnit unit) {
        this.start = System.currentTimeMillis();
        this.timeout = timeout;
        this.unit = unit;
    }

    public Boolean isTimeout() {
        return costUnit() > timeout;
    }

    public Long costTime() {
        return System.currentTimeMillis() - this.start;
    }

    public Long costUnit() {
        return this.unit.convert(costTime(), TimeUnit.MILLISECONDS);
    }
}
