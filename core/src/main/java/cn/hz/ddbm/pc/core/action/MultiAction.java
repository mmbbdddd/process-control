package cn.hz.ddbm.pc.core.action;


import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

import java.util.List;

/**
 * 支持aAction，bActioin，cAction.....组合成一个Action运行的写法。
 **/


public class MultiAction implements Action {
    String       actionNames;
    List<Action> actions;

    public MultiAction(String actionNames, List<Action> actions) {
        this.actionNames = actionNames;
        this.actions     = actions;
    }

    @Override
    public String beanName() {
        return actionNames;
    }

    @Override
    public void execute(FlowContext<?> ctx) throws Exception {
        for (Action action : this.actions) {
            action.execute(ctx);
        }
    }
}
