package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.example.IDCardDemo;
import org.springframework.stereotype.Component;

@Component
public class SendAction implements Action {

    public SendAction() {
    }

    @Override
    public String beanName() {
        return "sendAction";
    }

    @Override
    public Enum execute(FlowContext ctx) throws Exception {
        return null;
    }

}
