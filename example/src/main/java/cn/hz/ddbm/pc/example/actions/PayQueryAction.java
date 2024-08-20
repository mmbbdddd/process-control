package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.example.IDCardDemo;
import cn.hz.ddbm.pc.example.IDCardState;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

@Component
public class PayQueryAction implements Action.QueryAction<IDCardState> {

    public PayQueryAction() {
    }

    @Override
    public String beanName() {
        return "payAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {

    }


    @Override
    public IDCardState query(FlowContext<IDCardState, ?> ctx) throws Exception {

        IDCardState queryState = RandomUitl.random(Lists.newArrayList(IDCardState.payed_failover, IDCardState.init, IDCardState.payed));
        switch (queryState) {
            case payed_failover: {
                break;
            }
            case init: {
                IDCardDemo.account.incrementAndGet();
                IDCardDemo.freezed.decrementAndGet();
                break;
            }
        }

        return queryState;
    }
}
