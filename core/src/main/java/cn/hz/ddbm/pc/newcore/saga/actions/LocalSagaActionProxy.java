package cn.hz.ddbm.pc.newcore.saga.actions;


import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;

public class LocalSagaActionProxy {
    LocalSagaAction action;

    public LocalSagaActionProxy(Class<? extends SagaAction> actionType) {
        this.action = (LocalSagaAction) ProcessorService.getAction(actionType);
    }

    public SagaAction.QueryResult  doLocalSaga(FlowContext<SagaState> ctx) {
        return this.action.doLocalSaga(ctx);
    }

    public SagaAction.QueryResult doLocalSagaRollback(FlowContext<SagaState> ctx) {
        return this.action.doLocalSagaRollback(ctx);
    }
}
