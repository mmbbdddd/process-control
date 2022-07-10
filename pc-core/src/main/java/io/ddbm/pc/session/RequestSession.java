package io.ddbm.pc.session;

import io.ddbm.pc.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestSession implements Session {
    Map<String, Object> cache = new HashMap<>();

    @Override
    public void set(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(String key, Object defaultValue) {
        if (!cache.containsKey(key)) {
            cache.put(key, defaultValue);
        }
        return cache.get(key);
    }

    @Override
    public Set<String> keys() {
        return cache.keySet();
    }

    @Override
    public void clear(String key) {
        cache.remove(key);
    }

    @Override
    public void clearAll() {
        cache.clear();
    }
}
