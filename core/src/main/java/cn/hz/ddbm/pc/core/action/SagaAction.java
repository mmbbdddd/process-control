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


public interface SagaAction extends Action {

    String failover();

    @Override
    default void execute(FlowContext<?> ctx) {
        Router       router       = ctx.getAtomExecutor().getActionRouter().getRouter();
        String       flow         = ctx.getFlow().getName();
        Serializable flowId       = ctx.getId();
        String       failOverNode = failover();
        try {
            StatusManager statusManager = ctx.getFlow().getStatusManager();
            statusManager.setStatus(flow, flowId, FlowStatus.of(failOverNode),ctx.getProfile().getStatusTimeout(),ctx);
        } catch (IOException e) {
            throw new InterruptedFlowException(e);
        }
        executeTx(ctx);
    }

    void executeTx(FlowContext<?> ctx);

}
