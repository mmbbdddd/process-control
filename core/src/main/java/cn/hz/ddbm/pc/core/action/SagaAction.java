package cn.hz.ddbm.pc.core.action;


import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowStatus;
import cn.hz.ddbm.pc.core.Router;
import cn.hz.ddbm.pc.core.exception.InterruptedFlowException;
import cn.hz.ddbm.pc.core.support.StatusManager;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 23:32
 * @Version 1.0.0
 **/


public class SagaAction implements Action {
    String failover;
    Action action;

    public SagaAction(String failover, Action action) {
        this.failover = failover;
        this.action   = action;
    }

    @Override
    public String beanName() {
        return action.beanName();
    }

    @Override
    public void execute(FlowContext<?> ctx) throws Exception {
        Router       router       = ctx.getAtomExecutor().getActionRouter().getRouter();
        String       flow         = ctx.getFlow().getName();
        Serializable flowId       = ctx.getId();
        String       failOverNode = failover;
        try {
            StatusManager statusManager = ctx.getFlow().getProfile().getStatusManagerBean();
            statusManager.setStatus(flow, flowId, FlowStatus.of(failOverNode), ctx.getProfile().getStatusTimeout(), ctx);
        } catch (IOException e) {
            throw new InterruptedFlowException(e);
        }
        executeTx(ctx);
    }

    public void executeTx(FlowContext<?> ctx) throws Exception {
        action.execute(ctx);
    }
}


