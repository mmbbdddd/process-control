package cn.hz.ddbm.pc.session.redis;

import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.SessionManager;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

public class RedisSessionManager implements SessionManager {

    String                keyTemplate = "%s:%s;%s";

    @Override
    public String code() {
        return Coasts.SESSION_MANAGER_MEMORY;
    }

    @Override
    public void set(String flowName, String flowId, String key, Object value) {
        cache.put(String.format(keyTemplate, flowId, flowId, key), value);
    }

    @Override
    public Object get(String flowName, String flowId, String key) {
        return cache.getIfPresent(String.format(keyTemplate, flowId, flowId, key));
    }
}
