package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import org.springframework.stereotype.Component;

@Component
public class SendQueryAction implements Action {
    @Override
    public String beanName() {
        return "sendQueryAction";
    }

    @Override
    public Enum execute(FlowContext ctx) throws Exception {
        return null;
    }


}
