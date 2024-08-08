package cn.hz.ddbm.pc.core;

import cn.hz.ddbm.pc.core.support.MetricsTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MetricsTemplateMock implements MetricsTemplate {
    Map<String, AtomicInteger> map = new HashMap<>();
    @Override
    public void increment(String windows) {
        map.computeIfAbsent(windows, s -> new AtomicInteger(0)).incrementAndGet();
    }

    @Override
    public Integer get(String windows) {
        return map.computeIfAbsent(windows,s -> new AtomicInteger(0)).get();
    }
}
