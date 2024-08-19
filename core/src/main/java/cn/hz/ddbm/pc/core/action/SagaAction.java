package cn.hz.ddbm.pc.core.action;


import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.action.saga.SagaQueryAction;
import cn.hz.ddbm.pc.core.exception.wrap.ActionException;
import cn.hz.ddbm.pc.core.exception.InterruptedFlowException;
import cn.hz.ddbm.pc.core.exception.wrap.RouterException;
import cn.hz.ddbm.pc.core.log.Logs;
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


public class SagaAction<S extends Enum<S>> extends ActionBase<S> {


    public SagaAction(Fsm.FsmRecord<S> f, List<Plugin> plugins) {
        super(f, plugins);

    }

    @Override
    protected S failover() {
        return getFr().getFailover();
    }


    @Override
    public String beanName() {
        return getAction().beanName();
    }

    @Override
    public void execute(FlowContext<S, ?> ctx) throws ActionException, RouterException {
        String       flow   = ctx.getFlow().getName();
        Serializable flowId = ctx.getId();
        //首先将状态设置为容错状态
        ctx.getStatus().update(failover());
        try {
            StatusManager statusManager = InfraUtils.getStatusManager(ctx.getProfile().getStatusManager());
            statusManager.setStatus(flow, flowId, ctx.getStatus(), ctx.getProfile().getStatusTimeout(), ctx);
        } catch (IOException e) {
            //容错设置失败，则终止本次执行
            throw new InterruptedFlowException(e);
        }
        if (conditionIsTrue(ctx)) {
            ctx.setNextNode(null);
            super.execute(ctx);
        }
    }

    private boolean conditionIsTrue(FlowContext<S, ?> ctx) {
        try {
            SagaQueryAction<S> queryAction = InfraUtils.getBean(getFr().getConditionAction(), SagaQueryAction.class);
            return getFr().getConditions().contains(queryAction.queryCurrentStatus(ctx));
        }catch (Exception e){
            Logs.error.error("",e);
            return false;
        }
    }


    @Override
    public Set<S> maybeResult() {
        return null;
    }

    public S route(FlowContext<S, ?> ctx) {
        Assert.notNull(ctx.getNextNode(), "sagaAction 必须设置ctx.setNextNode()");
        return ctx.getNextNode();
    }
}


