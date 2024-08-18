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
public class ValidateAndNotifyUserAction implements ActionPluginAdapter<IDCardState> {
    @Override
    public String beanName() {
        return "validateAndNotifyUserAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {

    }

    @Override
    public void postAction(String name, IDCardState lastNode, FlowContext<IDCardState, ?> ctx) {
        IDCardState state = ctx.getStatus().getNode();
//        map.put("sendRouter", Sets.newSet(IDCardState.init, IDCardState.sended, IDCardState.miss_data, IDCardState.su, IDCardState.fail));
//        map.put("notifyRouter", Sets.newSet(IDCardState.init, IDCardState.miss_data, IDCardState.miss_data_fulled));

        switch (state){
            case init:{
                break;
            }
            case miss_data_fulled:{

            }
            case miss_data:{

            }
        }
    }
}
