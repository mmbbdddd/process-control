package cn.hz.ddbm.pc.core.action;


import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

import java.util.List;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/8 0:10
 * @Version 1.0.0
 **/


public class CombAction implements Action {
    String       actionNames;
    List<Action> actions;

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
