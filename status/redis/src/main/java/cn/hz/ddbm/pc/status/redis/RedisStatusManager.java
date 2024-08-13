package cn.hz.ddbm.pc.status.redis;

import cn.hz.ddbm.pc.core.FlowStatus;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.StatusManager;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;

public class RedisStatusManager implements StatusManager {
    @Override
    public String code() {
        return Coasts.STATUS_MANAGER_MEMORY;
    }

    @Override
    public void updateStatus(String flow, Serializable flowId, FlowStatus flowStatus) throws IOException {

    }
}
