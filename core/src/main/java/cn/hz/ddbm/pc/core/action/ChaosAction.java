package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;

public class ChaosAction implements SimpleAction {
    String actionName;

    @Override
    public String beanName() {
        return actionName;
    }


    @Override
    public void execute(FlowContext ctx) throws Exception {

    }


}
