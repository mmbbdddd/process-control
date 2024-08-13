package cn.hz.ddbm.pc.status.memory;

import cn.hz.ddbm.pc.core.FlowStatus;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.StatusManager;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;

public class MemoryStatusManager implements StatusManager {
    Cache<String, Object> cache = Caffeine.newBuilder()
            .initialCapacity(1)
            .maximumSize(100)
            .expireAfterWrite(Duration.ofHours(2))
            .build();

    @Override
    public String code() {
        return Coasts.STATUS_MANAGER_MEMORY;
    }

    @Override
    public void updateStatus(String flow, Serializable flowId, FlowStatus flowStatus) throws IOException {

    }

    @Override
    public FlowStatus getStatus(String flow, Serializable flowId) throws IOException {
        return null;
    }
}
