package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;
import cn.hz.ddbm.pc.example.IDCardDemo;
import cn.hz.ddbm.pc.example.IDCardState;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SendAction implements SimpleAction<IDCardState> {

    public SendAction() {
    }

    @Override
    public String beanName() {
        return "sendAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {

    }


}
