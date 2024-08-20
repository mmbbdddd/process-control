package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.example.PayState;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

@Component
public class SendAction implements Action.SagaAction<PayState> {

    public SendAction() {
    }

    @Override
    public String beanName() {
        return "sendAction";
    }

    @Override
    public void execute(FlowContext<PayState, ?> ctx) throws Exception {

    }


    @Override
    public PayState query(FlowContext<PayState, ?> ctx) throws Exception {
        return RandomUitl.random(Lists.newArrayList(PayState.sended_failover, PayState.sended));
    }

    @Override
    public PayState getExecuteResult(FlowContext<PayState, ?> ctx) {
        return RandomUitl.random(Lists.newArrayList(PayState.sended_failover, PayState.sended));
    }
}
