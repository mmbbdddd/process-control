package cn.hz.ddbm.pc.newcore.chaos;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.actions.LocalFsmAction;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaAction;

public class LocalChaosAction implements LocalFsmAction, LocalSagaAction {

    @Override
    public Object doLocalFsm(FlowContext<FsmState> ctx) throws Exception {
        return null;
    }

    @Override
    public QueryResult doLocalSagaRollback(FlowContext<SagaState> ctx) {
        return ChaosHandler.sagaLocalResult();
    }

    @Override
    public SagaAction.QueryResult doLocalSaga(FlowContext<SagaState> ctx) {
        return ChaosHandler.sagaLocalResult();
    }
}
