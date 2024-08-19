package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;
import cn.hz.ddbm.pc.core.plugins.ActionPluginAdapter;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.example.IDCardDemo;
import cn.hz.ddbm.pc.example.IDCardState;
import org.assertj.core.util.Lists;
import org.mockito.internal.util.collections.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SendAction implements ActionPluginAdapter<IDCardState> {

    public SendAction() {
    }

    @Override
    public String beanName() {
        return "sendAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {
        ctx.setNextNode(RandomUitl.random(Lists.newArrayList(IDCardState.sended_failover,IDCardState.sended)));

    }
    @Override
    public void postAction(String name, IDCardState lastNode, FlowContext<IDCardState, ?> ctx) {
        IDCardState state = ctx.getStatus().getName();
//        map.put("payRouter", Sets.newSet(IDCardState.payed_failover,IDCardState.init, IDCardState.payed));
//        map.put("sendRouter", Sets.newSet(IDCardState.sended_failover,IDCardState.payed,IDCardState.sended, IDCardState.su, IDCardState.fail));
//        switch (state){
//            case sended_failover:{
//                break;
//            }
//            case payed:{
//                break;
//            }
//            case sended:{
//                break;
//            }
//            case su:{
//                IDCardDemo.freezed.decrementAndGet();
//                IDCardDemo.bank.incrementAndGet();
//                break;
//            }
//            case fail:{
//                IDCardDemo.account.incrementAndGet();
//                IDCardDemo.freezed.decrementAndGet();
//                break;
//            }
//        }
    }

}
