package cn.hz.ddbm.pc.saga.actions;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaAction;

public class SagaEndAction implements LocalSagaAction {

    @Override
    public Boolean doLocalSagaRollback(FlowContext<SagaState> ctx) {
return null;
    }

    @Override
    public Boolean doLocalSaga(FlowContext<SagaState> ctx) {
        return null;
    }
}
