package cn.hz.ddbm.pc.test.support;

import cn.hz.ddbm.pc.core.support.StatisticsSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsTemplateMock implements StatisticsSupport {
    Map<String, AtomicLong> map = new HashMap<>();

    @Override
    public void increment(String windows) {
        map.computeIfAbsent(windows, s -> new AtomicLong(0)).incrementAndGet();
    }

    @Override
    public Long get(String windows) {
        return map.computeIfAbsent(windows, s -> new AtomicLong(0)).get();
    }
}
