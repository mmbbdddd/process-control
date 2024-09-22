package cn.hz.ddbm.pc.newcore.infra.impl;

import cn.hz.ddbm.pc.newcore.State;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.infra.StatisticsManager;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleStatisticsManager implements StatisticsManager {


    ConcurrentMap<String, AtomicLong> cache;

    public SimpleStatisticsManager() {
        cache = new ConcurrentHashMap<>();
    }


    @Override
    public Coast.StatisticsType code() {
        return Coast.StatisticsType.simple;
    }

    @Override
    public void increment(String flowName, Serializable flowId, State node, String variable) {
        String realKey = String.format("%s:%s:%s:%s", flowName, flowId, node.code(), variable);
        cache.computeIfAbsent(realKey, s -> new AtomicLong(0)).incrementAndGet();
    }

    @Override
    public Long get(String flowName, Serializable flowId, State node, String variable) {
        String realKey = String.format("%s:%s:%s:%s", flowName, flowId, node.code(), variable);
        return cache.computeIfAbsent(realKey, s -> new AtomicLong(0)).get();
    }
}
