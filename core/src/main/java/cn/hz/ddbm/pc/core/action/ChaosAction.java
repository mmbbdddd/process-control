package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

public class ChaosAction<S extends Enum<S>> implements Action<S> {
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
