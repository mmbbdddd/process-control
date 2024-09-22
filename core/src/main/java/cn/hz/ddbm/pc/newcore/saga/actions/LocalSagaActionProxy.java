package cn.hz.ddbm.pc.newcore.saga.actions;


import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;

public class LocalSagaActionProxy {
    Class<? extends SagaAction> actionType;

    public LocalSagaActionProxy(Class<? extends SagaAction> actionType) {
        this.actionType = actionType;
    }

    public SagaAction.QueryResult  doLocalSaga(FlowContext<SagaState> ctx) {
        return getAction().doLocalSaga(ctx);
    }

    public SagaAction.QueryResult doLocalSagaRollback(FlowContext<SagaState> ctx) {
        return getAction().doLocalSagaRollback(ctx);
    }

    LocalSagaAction getAction(){
        return (LocalSagaAction) ProcessorService.getAction(actionType);
    }
}
