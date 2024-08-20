package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.example.IDCardState;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

@Component
public class SendAction implements Action.SagaAction<IDCardState> {

    public SendAction() {
    }

    @Override
    public String beanName() {
        return "sendAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {

    }


    @Override
    public IDCardState query(FlowContext<IDCardState, ?> ctx) throws Exception {
        return RandomUitl.random(Lists.newArrayList(IDCardState.sended_failover, IDCardState.sended));
    }

    @Override
    public IDCardState getExecuteResult(FlowContext<IDCardState, ?> ctx) {
        return RandomUitl.random(Lists.newArrayList(IDCardState.sended_failover, IDCardState.sended));
    }
}
