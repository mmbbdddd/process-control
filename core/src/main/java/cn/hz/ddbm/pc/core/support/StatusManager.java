package cn.hz.ddbm.pc.core.support;


import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowStatus;
import cn.hz.ddbm.pc.core.exception.StatusException;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 21:29
 * @Version 1.0.0
 **/


public interface StatusManager {
    String code();

    void setStatus(String flow, Serializable flowId, FlowStatus flowStatus, Integer timeout,FlowContext<?> ctx) throws IOException;

    FlowStatus getStatus(String flow, Serializable flowId) throws IOException;

    default void flush(FlowContext<?> ctx) throws StatusException {
        try {
            setStatus(ctx.getFlow().getName(), ctx.getId(), ctx.getStatus(), ctx.getProfile().getStatusTimeout(),ctx);
        } catch (IOException e) {
            throw new StatusException(e);
        }
    }
}
