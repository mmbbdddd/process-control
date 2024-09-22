package cn.hz.ddbm.pc.newcore.infra;


import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.exception.IdempotentException;
import cn.hz.ddbm.pc.newcore.exception.StatusException;

import java.io.Serializable;

/**
 * @Description 状态接口
 * @Author wanglin
 * @Date 2024/8/7 21:29
 * @Version 1.0.0
 **/


public interface StatusManager extends ValueObject<Coast.StatusType> {
    Coast.StatusType code();

    <T> void setStatus(String key, T status, Integer timeout) throws StatusException;

    <T> T getStatus(String key, Class<T> type) throws StatusException;

    void idempotent(String key) throws IdempotentException;

    void unidempotent(String key);

}
