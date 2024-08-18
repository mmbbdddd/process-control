package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

public class ChaosAction implements Action {
    String actionName;

    @Override
    public String beanName() {
        return actionName;
    }

    @Override
    public Enum execute(FlowContext ctx) throws Exception {
        return null;
    }


}
