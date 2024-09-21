package cn.hz.ddbm.pc.saga.actions;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaAction;
import cn.hz.ddbm.pc.saga.PayTest;

public class SagaPayAction implements LocalSagaAction {


    @Override
    public Boolean doLocalSagaRollback(FlowContext<SagaState> ctx) {
        PayTest.freezed.incrementAndGet();
        PayTest.bank.decrementAndGet();
        return null;
    }

    @Override
    public Boolean doLocalSaga(FlowContext<SagaState> ctx) {
        PayTest.freezed.decrementAndGet();
        PayTest.bank.incrementAndGet();
        return null;
    }
}
