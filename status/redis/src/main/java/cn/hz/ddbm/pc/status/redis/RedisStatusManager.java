package cn.hz.ddbm.pc.status.redis;

import cn.hz.ddbm.pc.core.FlowStatus;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.StatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;

public class RedisStatusManager implements StatusManager {
    @Autowired
    RedisTemplate<String,FlowStatus> redisTemplate;
    @Override
    public String code() {
        return Coasts.STATUS_MANAGER_MEMORY;
    }
    String keyTemplate = "%s:%s";

    @Override
    public void setStatus(String flow, Serializable flowId, FlowStatus flowStatus, Long timeout) throws IOException {
        redisTemplate.opsForValue().set(String.format(keyTemplate,flow,flowId),flowStatus);
    }

    @Override
    public FlowStatus getStatus(String flow, Serializable flowId) throws IOException {
        return null;
    }

}
