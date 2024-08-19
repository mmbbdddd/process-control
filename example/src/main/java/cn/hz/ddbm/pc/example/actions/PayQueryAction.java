package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.plugins.ActionPluginAdapter;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.example.IDCardDemo;
import cn.hz.ddbm.pc.example.IDCardState;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import static cn.hz.ddbm.pc.example.IDCardState.init;

@Component
public class PayQueryAction implements ActionPluginAdapter<IDCardState> {

    public PayQueryAction() {
    }

    @Override
    public String beanName() {
        return "payAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {
        IDCardState lastState = ctx.getStatus().getName();
        if(lastState.equals(init))return;

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
        ctx.setNextNode(queryState);
    }

    @Override
    public void postAction(String name, IDCardState lastNode, FlowContext<IDCardState, ?> ctx) {
//        IDCardState state = ctx.getStatus().getName();
////        map.put("payRouter", Sets.newSet(IDCardState.payed_failover,IDCardState.init, IDCardState.payed));
////        map.put("sendRouter", Sets.newSet(IDCardState.sended_failover,IDCardState.init,IDCardState.sended, IDCardState.su, IDCardState.fail));
//        switch (state){
//            case payed_failover:{
//                break;
//            }
//            case init:{
//                IDCardDemo.account.incrementAndGet();
//                IDCardDemo.freezed.decrementAndGet();
//                break;
//            }
//            case payed:{
//                break;
//            }
//        }


    }

}
