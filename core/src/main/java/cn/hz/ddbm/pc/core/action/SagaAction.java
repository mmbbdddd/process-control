package cn.hz.ddbm.pc.core.action;


import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.exception.wrap.ActionException;
import cn.hz.ddbm.pc.core.exception.InterruptedFlowException;
import cn.hz.ddbm.pc.core.exception.wrap.RouterException;
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


public class SagaAction<S extends Enum<S>> extends ActionBase<S>   {
    Set<S>    maybeResult;


    public SagaAction(Fsm.FsmRecord<S> f ,Set<S>    maybeResult, List<Plugin> plugins) {
        super(f.getType(),f.getFrom(),f.getEvent(),f.getActionDsl(),f.getFailover(),f.getTo(),plugins);
        this.maybeResult = maybeResult;
    }

    @Override
    protected S failover() {
        return getFailover();
    }


    @Override
    public String beanName() {
        return getAction().beanName();
    }

    @Override
    public void execute(FlowContext<S, ?> ctx) throws ActionException, RouterException {
        String       flow         = ctx.getFlow().getName();
        Serializable flowId       = ctx.getId();
        S            failOverNode = getFailover();
        try {
            StatusManager statusManager = InfraUtils.getStatusManager(ctx.getProfile().getStatusManager());
            statusManager.setStatus(flow, flowId, ctx.getStatus(), ctx.getProfile().getStatusTimeout(), ctx);
            ctx.setNextNode(null);
            super.execute(ctx);
        } catch (IOException e) {
            throw new InterruptedFlowException(e);
        }
    }


    @Override
    public Set<S> maybeResult() {
        return maybeResult;
    }

    public S route(FlowContext<S, ?> ctx) {
        Assert.notNull(ctx.getNextNode(),"sagaAction 必须设置ctx.setNextNode()");
        return ctx.getNextNode();
    }
}


