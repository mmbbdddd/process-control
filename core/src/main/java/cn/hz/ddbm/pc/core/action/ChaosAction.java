package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;

public class ChaosAction<S extends Enum<S>> implements SimpleAction<S> {
    String actionName;

    @Override
    public String beanName() {
        return actionName;
    }


    @Override
    public void execute(FlowContext<S, ?> ctx) throws Exception {

        int i = 0;
    }


}
