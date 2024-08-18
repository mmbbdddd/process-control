package cn.hz.ddbm.pc.test;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;

import java.util.Set;

public class TestAction implements SimpleAction {
    @Override
    public String beanName() {
        return "test";
    }



    @Override
    public void execute(FlowContext ctx) throws Exception {

    }


}
