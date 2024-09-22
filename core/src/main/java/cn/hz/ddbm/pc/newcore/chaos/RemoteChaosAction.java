package cn.hz.ddbm.pc.newcore.chaos;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.actions.RemoteFsmAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import cn.hz.ddbm.pc.newcore.saga.actions.RemoteSagaAction;

public class RemoteChaosAction implements RemoteFsmAction, RemoteSagaAction {


    @Override
    public void doRemoteSaga(FlowContext<SagaState> ctx) {

    }

    @Override
    public QueryResult remoteSagaQuery(FlowContext<SagaState> ctx) {
        return ChaosConfig.sagaRemoteResult();
    }

    @Override
    public void doRemoteSagaRollback(FlowContext<SagaState> ctx) {

    }

    @Override
    public QueryResult remoteSagaRollbackQuery(FlowContext<SagaState> ctx) {
        return ChaosConfig.sagaRemoteResult();
    }


    @Override
    public void remoteFsm(FlowContext<FsmState> ctx)   {

    }

    @Override
    public Object remoteFsmQuery(FlowContext<FsmState> ctx)   {
        return null;
    }
}
