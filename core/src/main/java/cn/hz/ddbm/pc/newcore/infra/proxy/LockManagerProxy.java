package cn.hz.ddbm.pc.newcore.infra.proxy;

import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.exception.LockException;
import cn.hz.ddbm.pc.newcore.infra.LockManager;

public class LockManagerProxy   {
    LockManager locker;

    public LockManagerProxy(LockManager t) {
        this.locker = t;
    }



    public void tryLock(String key, Integer timeout) throws LockException {
        try {
            locker.tryLock(key, timeout);
        } catch (LockException e) {
            throw e;
        } catch (Exception e) {
            throw new LockException(e);
        }
    }


    public void releaseLock(String key) throws LockException {
        try {
            locker.releaseLock(key);
        } catch (LockException e) {
            throw e;
        } catch (Exception e) {
            throw new LockException(e);
        }
    }

}
