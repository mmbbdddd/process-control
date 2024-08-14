package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.coast.Coasts;

public class NoneAction implements Action {
    @Override
    public String beanName() {
        return Coasts.NONE;
    }

    @Override
    public void execute(FlowContext<?> ctx) throws Exception {

    }
}