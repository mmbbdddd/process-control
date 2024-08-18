package cn.hz.ddbm.pc.core.action;


import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowStatus;
import cn.hz.ddbm.pc.core.exception.InterruptedFlowException;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 23:32
 * @Version 1.0.0
 **/


public class SagaAction<S extends Enum<S>> implements Action<S> {
    S         failover;
    Action<S> action;

    public SagaAction(S failover, Action<S> action) {
        this.failover = failover;
        this.action   = action;
    }

    @Override
    public String beanName() {
        return action.beanName();
    }

    @Override
    public S execute(FlowContext<S, ?> ctx) throws Exception {
        String       flow         = ctx.getFlow().getName();
        Serializable flowId       = ctx.getId();
        S            failOverNode = failover;
        try {
            StatusManager statusManager = InfraUtils.getStatusManager(ctx.getProfile().getStatusManager());
            statusManager.setStatus(flow, flowId, FlowStatus.of(failOverNode), ctx.getProfile().getStatusTimeout(), ctx);
        } catch (IOException e) {
            throw new InterruptedFlowException(e);
        }
        executeTx(ctx);
        return null;
    }

    public void executeTx(FlowContext<S, ?> ctx) throws Exception {
        action.execute(ctx);
    }
}


