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
public class SendQueryAction implements Action.QueryAction<IDCardState> {
    @Override
    public String beanName() {
        return "sendQueryAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {

    }


    @Override
    public IDCardState query(FlowContext<IDCardState, ?> ctx) throws Exception {
        IDCardState queryState = RandomUitl.random(Lists.newArrayList(IDCardState.sended_failover, IDCardState.payed, IDCardState.sended, IDCardState.su, IDCardState.fail));
        switch (queryState) {
            case sended_failover: {
                break;
            }
            case su: {
                Logs.flow.info("{},{}支付成功", ctx.getFlow().getName(), ctx.getId());
                IDCardDemo.freezed.decrementAndGet();
                IDCardDemo.bank.incrementAndGet();
                break;
            }
            case fail: {
                Logs.flow.info("{},{}支付失败", ctx.getFlow().getName(), ctx.getId());
                IDCardDemo.account.incrementAndGet();
                IDCardDemo.freezed.decrementAndGet();
                break;
            }
        }
        return queryState;
    }
}
