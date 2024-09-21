package cn.hz.ddbm.pc.newcore.saga.actions;


import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;

public class LocalSagaActionProxy {
    LocalSagaAction action;

    public LocalSagaActionProxy(Class<? extends SagaAction> actionType) {
        this.action = (LocalSagaAction) SpringUtil.getBean(actionType);
    }

    public Boolean  doLocalSaga(FlowContext<SagaState> ctx) {
        return this.action.doLocalSaga(ctx);
    }

    public Boolean doLocalSagaRollback(FlowContext<SagaState> ctx) {
        return this.action.doLocalSagaRollback(ctx);
    }
}
