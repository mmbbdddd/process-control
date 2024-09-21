package cn.hz.ddbm.pc.saga.actions;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import cn.hz.ddbm.pc.newcore.saga.actions.RemoteSagaAction;

public class SagaSendAction implements RemoteSagaAction {


    @Override
    public void doRemoteSaga(FlowContext<SagaState> ctx) {

    }

    @Override
    public SagaWorker.Offset remoteSagaQuery(FlowContext<SagaState> ctx) {
        Boolean result = Math.random() > 0.5;


        return null;
    }

    @Override
    public void doRemoteSagaRollback(FlowContext<SagaState> ctx) {

    }

    @Override
    public SagaWorker.Offset remoteSagaRollbackQuery(FlowContext<SagaState> ctx) {
        Boolean result = Math.random() > 0.5;

        return null;
    }
}
