package cn.hz.ddbm.pc.example.actions;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.plugins.ActionPluginAdapter;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.example.IDCardDemo;
import cn.hz.ddbm.pc.example.IDCardState;
import org.assertj.core.util.Lists;
import org.mockito.internal.util.collections.Sets;
import org.springframework.stereotype.Component;

@Component
public class PayAction implements ActionPluginAdapter<IDCardState> {

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
        Logs.flow.info("{},{}支付扣款",ctx.getFlow().getName(),ctx.getId());
        ctx.setNextNode(RandomUitl.random(Lists.newArrayList(IDCardState.payed_failover, IDCardState.payed)));
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
