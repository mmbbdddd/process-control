package cn.hz.ddbm.pc.newcore.saga.actions;


import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;

public class RemoteSagaActionProxy {
    private final Class<? extends SagaAction> actionType;

    public RemoteSagaActionProxy(Class<? extends SagaAction> actionType) {
        this.actionType = actionType;
    }

    public void doRemoteSaga(FlowContext<SagaState> ctx) {
        getAction().doRemoteSaga(ctx);
    }

    public RemoteSagaAction.QueryResult remoteSagaQuery(FlowContext<SagaState> ctx) {
        return getAction().remoteSagaQuery(ctx);
    }

    public void doRemoteSagaRollback(FlowContext<SagaState> ctx) {
        getAction().doRemoteSagaRollback(ctx);
    }

    public RemoteSagaAction.QueryResult remoteSagaRollbackQuery(FlowContext<SagaState> ctx) {
        return getAction().remoteSagaRollbackQuery(ctx);
    }

    RemoteSagaAction getAction() {
        return (RemoteSagaAction) ProcessorService.getAction(actionType);
    }
}
