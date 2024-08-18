package cn.hz.ddbm.pc.core.action;


import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.action.dsl.SimpleAction;
import cn.hz.ddbm.pc.core.exception.ActionException;
import cn.hz.ddbm.pc.core.exception.InterruptedFlowException;
import cn.hz.ddbm.pc.core.exception.RouterException;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 23:32
 * @Version 1.0.0
 **/


public class SagaAction<S extends Enum<S>> extends ActionBase<S> implements Action<S> {
    Action<S> action;
    Set<S>    maybe;


    public SagaAction(Fsm.FsmRecord<S> f , List<Plugin> plugins) {
        super(f.getType(),f.getFrom(),f.getEvent(),f.getActionDsl(),f.getFailover(),f.getTo(),plugins);
    }

    @Override
    protected S failover() {
        return getFailover();
    }


    @Override
    public String beanName() {
        return action.beanName();
    }

    @Override
    public void execute(FlowContext<S, ?> ctx) throws ActionException, RouterException {
        String       flow         = ctx.getFlow().getName();
        Serializable flowId       = ctx.getId();
        S            failOverNode = getFailover();
        try {
            StatusManager statusManager = InfraUtils.getStatusManager(ctx.getProfile().getStatusManager());
            statusManager.setStatus(flow, flowId, FlowStatus.of(failOverNode), ctx.getProfile().getStatusTimeout(), ctx);
            super.execute(ctx);
        } catch (IOException e) {
            throw new InterruptedFlowException(e);
        }
    }


    @Override
    public Set<S> maybeResult() {
        return maybe;
    }

    public S route(FlowContext<S, ?> ctx) {
        return ctx.getNextNode();
    }
}


