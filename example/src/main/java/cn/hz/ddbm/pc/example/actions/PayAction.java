package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.example.IDCardDemo;
import cn.hz.ddbm.pc.example.IDCardState;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

@Component
public class PayAction implements Action.SagaAction<IDCardState> {

    public PayAction() {
    }

    @Override
    public String beanName() {
        return "payAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {
        IDCardDemo.account.decrementAndGet();
        IDCardDemo.freezed.incrementAndGet();
        Logs.flow.info("{},{}支付扣款", ctx.getFlow().getName(), ctx.getId());
    }


    @Override
    public IDCardState query(FlowContext<IDCardState, ?> ctx) throws Exception {
        return RandomUitl.random(Lists.newArrayList(IDCardState.init, IDCardState.payed));
    }

    @Override
    public IDCardState getExecuteResult(FlowContext<IDCardState, ?> ctx) {
        return RandomUitl.random(Lists.newArrayList(IDCardState.payed_failover, IDCardState.payed));
    }
}
