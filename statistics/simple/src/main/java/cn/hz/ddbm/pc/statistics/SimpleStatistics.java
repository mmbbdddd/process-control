package cn.hz.ddbm.pc.statistics;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.support.StatisticsSupport;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleStatistics implements StatisticsSupport {

    Integer cacheSize;
    Integer hours;

    public SimpleStatistics(Integer cacheSize, Integer hours) {
        Assert.notNull(cacheSize, "cacheSize is null");
        Assert.notNull(hours, "hours is null");
        this.cacheSize = cacheSize;
        this.hours     = hours;
    }

    Cache<String, AtomicLong> cache = Caffeine.newBuilder()
            .initialCapacity(1)
            .maximumSize(cacheSize)
            .expireAfterWrite(Duration.ofHours(hours))
            .build();

    @Override
    public void increment(String windows) {
        cache.get(windows, s -> new AtomicLong(0)).incrementAndGet();
    }

    @Override
    public Long get(String windows) {
        return cache.get(windows, s -> new AtomicLong(0)).get();
    }
}
