package cn.hz.ddbm.pc.core.action.dsl;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

import java.util.Set;

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
