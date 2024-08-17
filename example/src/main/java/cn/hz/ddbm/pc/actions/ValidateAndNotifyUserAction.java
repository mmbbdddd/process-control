package cn.hz.ddbm.pc.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

public class ValidateAndNotifyUserAction implements Action {
    @Override
    public String beanName() {
        return "validateAndNotifyUserAction";
    }

    @Override
    public void execute(FlowContext<?> ctx) throws Exception {

    }
}
