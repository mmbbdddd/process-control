package cn.hz.ddbm.pc.newcore.infra.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.exception.IdempotentException;
import cn.hz.ddbm.pc.newcore.exception.StatusException;
import cn.hz.ddbm.pc.newcore.infra.StatusManager;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JvmStatusManager implements StatusManager {
    ConcurrentMap<String, String>  statusMap;
    ConcurrentMap<String, Boolean> idempotentRecords;

    public JvmStatusManager() {
        this.statusMap         = new ConcurrentHashMap<>();
        this.idempotentRecords = new ConcurrentHashMap<>();
    }

    @Override
    public Coast.StatusType code() {
        return Coast.StatusType.jvm;
    }

    @Override
    public <T> void setStatus(String key, T status, Integer timeout) throws StatusException {

    }

    @Override
    public <T> T getStatus(String key, Class<T> type) throws StatusException {
        try {
            String status = statusMap.get(key);
            if (StrUtil.isEmpty(status)) {
                return null;
            }
            return JSONUtil.toBean(status, type);
        } catch (Exception e) {
            throw new StatusException(e);
        }
    }


    @Override
    public void idempotent(String key) throws IdempotentException {
        if (idempotentRecords.containsKey(key)) {
            throw new IdempotentException(String.format("交易幂等:" + key));
        } else {
            idempotentRecords.put(key, true);
        }
    }

    @Override
    public void unidempotent(String key) {
        idempotentRecords.remove(key);
    }
}
