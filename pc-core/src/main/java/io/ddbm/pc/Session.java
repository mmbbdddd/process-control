package io.ddbm.pc;

import java.util.Set;

public interface Session {
    void set(String key, Object value);

    Object get(String key, Object defaultValue);

    Set<String> keys();

    void clear(String key);

    void clearAll();
}
