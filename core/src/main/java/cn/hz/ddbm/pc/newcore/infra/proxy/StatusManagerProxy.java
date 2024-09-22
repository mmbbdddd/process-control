package cn.hz.ddbm.pc.newcore.infra.proxy;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.exception.IdempotentException;
import cn.hz.ddbm.pc.newcore.exception.StatusException;
import cn.hz.ddbm.pc.newcore.infra.StatusManager;

import java.io.Serializable;

public class StatusManagerProxy {
    StatusManager statusManager;

    public StatusManagerProxy(StatusManager t) {
        this.statusManager = t;
    }


    public <T> void setStatus(FlowContext ctx) throws StatusException {
        try {
            String app = ctx.getFlow().flowAttrs().getNamespace();
            String id  = ctx.getId();
            String key = String.format("status:%s:%s", app, id);
            statusManager.setStatus(key, ctx.getState(), 10000);
        } catch (Exception e) {
            throw new StatusException(e);
        }

    }

    public <T> T getStatus(String key, Class<T> type) throws StatusException {
        try {
            return statusManager.getStatus(key,type);
        } catch (Exception e) {
            throw new StatusException(e);
        }
    }

    public void idempotent(String key) throws IdempotentException {
        try {
//            SpringUtil.getBean(ChaosHandler.class).status();
            statusManager.idempotent(key);
        } catch (IdempotentException e) {
            throw e;
        } catch (Exception e) {
            throw new IdempotentException(e);
        }
    }

    public void unidempotent(String key) {
        try {
//            SpringUtil.getBean(ChaosHandler.class).status();
            statusManager.unidempotent(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
