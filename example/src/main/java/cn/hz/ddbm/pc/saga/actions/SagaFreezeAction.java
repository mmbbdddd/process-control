package cn.hz.ddbm.pc.saga.actions;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaAction;
import cn.hz.ddbm.pc.saga.PayTest;

public class SagaFreezeAction implements LocalSagaAction {


    @Override
    public SagaAction.QueryResult doLocalSagaRollback(FlowContext<SagaState> ctx) {
        PayTest.account.incrementAndGet();
        PayTest.freezed.decrementAndGet();
        return null;
    }

    @Override
    public SagaAction.QueryResult doLocalSaga(FlowContext<SagaState> ctx) {
        PayTest.account.decrementAndGet();
        PayTest.freezed.incrementAndGet();
        return null;
    }
}
