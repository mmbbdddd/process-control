package io.ddbm.pc.session;

import io.ddbm.pc.Session;

import java.util.Set;

public class RedisSession implements Session {
    @Override
    public void set(String key, Object value) {

    }

    @Override
    public Object get(String key, Object defaultValue) {
        return null;
    }

    @Override
    public Set<String> keys() {
        return null;
    }

    @Override
    public void clear(String key) {

    }

    @Override
    public void clearAll() {

    }
}
