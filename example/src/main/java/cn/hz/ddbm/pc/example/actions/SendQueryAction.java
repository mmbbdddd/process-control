package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;
import cn.hz.ddbm.pc.core.plugins.ActionPluginAdapter;
import cn.hz.ddbm.pc.example.IDCardDemo;
import cn.hz.ddbm.pc.example.IDCardState;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SendQueryAction implements ActionPluginAdapter<IDCardState> {
    @Override
    public String beanName() {
        return "sendQueryAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {

    }
    @Override
    public void postAction(String name, IDCardState lastNode, FlowContext<IDCardState, ?> ctx) {
        IDCardState state = ctx.getStatus().getName();
        switch (state){
            case init:{
                IDCardDemo.account.incrementAndGet();
                IDCardDemo.freezed.decrementAndGet();
                break;
            }

            case miss_data:{

            }
            case su:{
                IDCardDemo.freezed.decrementAndGet();
                break;
            }
            case fail:{
                IDCardDemo.account.incrementAndGet();
                IDCardDemo.freezed.decrementAndGet();
                break;
            }
        }

    }

}
