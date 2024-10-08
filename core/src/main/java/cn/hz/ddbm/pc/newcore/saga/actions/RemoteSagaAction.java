package cn.hz.ddbm.pc.newcore.saga.actions;


import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;

public interface RemoteSagaAction extends SagaAction {
    void doRemoteSaga(FlowContext<SagaState> ctx);

    QueryResult remoteSagaQuery(FlowContext<SagaState> ctx);

    void doRemoteSagaRollback(FlowContext<SagaState> ctx);

    QueryResult remoteSagaRollbackQuery(FlowContext<SagaState> ctx);

}
