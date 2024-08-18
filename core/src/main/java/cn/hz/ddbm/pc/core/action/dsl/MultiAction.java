package cn.hz.ddbm.pc.core.action.dsl;


import cn.hz.ddbm.pc.core.FlowContext;

import java.util.List;

/**
 * 支持aAction，bActioin，cAction.....组合成一个Action运行的写法。
 **/


public class MultiAction implements SimpleAction{
    String              actionNames;
    List<SimpleAction> actions;

    public MultiAction(String actionNames, List<SimpleAction> actions) {
        this.actionNames = actionNames;
        this.actions     = actions;
    }

    @Override
    public String beanName() {
        return actionNames;
    }

    @Override
    public void execute(FlowContext ctx) throws Exception {
        for (SimpleAction action : this.actions) {
            action.execute(ctx);
        }
    }




}
