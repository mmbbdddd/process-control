package cn.hz.ddbm.pc.core.action;


import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

import java.util.List;

/**
 * 支持aAction，bActioin，cAction.....组合成一个Action运行的写法。
 **/


public class MultiAction<S extends Enum<S>> implements Action<S> {
    String          actionNames;
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
    public S execute(FlowContext<S, ?> ctx) throws Exception {
        for (Action<S> action : this.actions) {
            action.execute(ctx);
        }
        return null;
    }
}
