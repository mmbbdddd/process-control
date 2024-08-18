package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import org.springframework.stereotype.Component;

@Component
public class ValidateAndNotifyUserAction implements Action {
    @Override
    public String beanName() {
        return "validateAndNotifyUserAction";
    }

    @Override
    public Enum execute(FlowContext ctx) throws Exception {
        return null;
    }


}
