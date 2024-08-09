package cn.hz.ddbm.pc.test;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

public class TestAction implements Action {
    @Override
    public String beanName() {
        return "test";
    }

    @Override
    public void execute(FlowContext<?> ctx) throws Exception {

    }
}
