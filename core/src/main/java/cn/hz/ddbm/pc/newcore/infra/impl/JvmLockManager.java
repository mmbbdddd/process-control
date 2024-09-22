package cn.hz.ddbm.pc.newcore.infra.impl;

import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.exception.LockException;
import cn.hz.ddbm.pc.newcore.infra.LockManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class JvmLockManager implements LockManager {
    ConcurrentMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @Override
    public Coast.LockType code() {
        return Coast.LockType.jvm;
    }

    @Override
    public void tryLock(String key, Integer timeout) throws LockException {
        try {
            lockMap.computeIfAbsent(key, s -> new ReentrantLock()).tryLock(timeout, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
    }

    @Override
    public void releaseLock(String key) throws LockException {
        try {
            lockMap.computeIfAbsent(key, s -> new ReentrantLock()).unlock();
        } catch (Exception e) {
            throw new LockException(e);
        }
    }
}
