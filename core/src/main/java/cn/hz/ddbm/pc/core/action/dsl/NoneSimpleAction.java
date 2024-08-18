package cn.hz.ddbm.pc.core.action.dsl;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;

public class NoneSimpleAction implements SimpleAction {
    String actionDsl;

    public NoneSimpleAction(String actionDsl) {
        this.actionDsl = actionDsl;
    }

    @Override
    public String beanName() {
        return actionDsl;
    }

    @Override
    public void execute(FlowContext ctx) throws Exception {

    }
}
