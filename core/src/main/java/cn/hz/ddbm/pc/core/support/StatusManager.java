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

    void updateStatus(String flow, Serializable flowId, FlowStatus flowStatus) throws IOException;

    default void flush(FlowContext<?> ctx)  throws StatusException {

    }
}
