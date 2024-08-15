package cn.hz.ddbm.pc.core.support;

import java.io.IOException;

public interface Locker {
    void tryLock(String key, Integer timeout) throws IOException;

    void releaseLock(String key) throws IOException;
}
