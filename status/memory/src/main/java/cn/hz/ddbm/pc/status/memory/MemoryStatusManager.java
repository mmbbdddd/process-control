package cn.hz.ddbm.pc.status.memory;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowStatus;
import cn.hz.ddbm.pc.core.support.StatusManager;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;

public class MemoryStatusManager implements StatusManager {
    private final Integer cacheSize;
    private final Integer hours;
    Cache<String, FlowStatus> cache;
    String                    keyTemplate = "%s:%s";

    public MemoryStatusManager(Integer cacheSize, Integer hours) {
        Assert.notNull(cacheSize, "cacheSize is null");
        Assert.notNull(hours, "hours is null");
        this.cacheSize = cacheSize;
        this.hours     = hours;
        this.cache     = Caffeine.newBuilder()
                                 .initialCapacity(cacheSize > 256 ? cacheSize / 8 : cacheSize)
                                 .maximumSize(cacheSize)
                                 .expireAfterWrite(Duration.ofHours(hours))
                                 .build();
    }

    @Override
    public Type code() {
        return Type.memory;
    }

    @Override
    public void setStatus(String flow, Serializable flowId, FlowStatus flowStatus, Integer timeout, FlowContext<?> ctx) throws IOException {
        cache.put(String.format(keyTemplate, flow, flowId), flowStatus);
    }


    @Override
    public FlowStatus getStatus(String flow, Serializable flowId) throws IOException {
        return cache.getIfPresent(String.format(keyTemplate, flow, flowId));
    }
}
