package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.example.PayTest;
import cn.hz.ddbm.pc.example.PayState;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

@Component
public class PayAction implements Action.SagaAction<PayState> {

    public PayAction() {
    }

    @Override
    public String beanName() {
        return "payAction";
    }

    @Override
    public void execute(FlowContext<PayState, ?> ctx) throws Exception {
        PayTest.account.decrementAndGet();
        PayTest.freezed.incrementAndGet();
        Logs.flow.info("{},{}支付扣款", ctx.getFlow().getName(), ctx.getId());
    }


    @Override
    public PayState query(FlowContext<PayState, ?> ctx) throws Exception {
        return RandomUitl.random(Lists.newArrayList(PayState.init, PayState.payed));
    }

    @Override
    public PayState getExecuteResult(FlowContext<PayState, ?> ctx) {
        return RandomUitl.random(Lists.newArrayList(PayState.payed_failover, PayState.payed));
    }
}
