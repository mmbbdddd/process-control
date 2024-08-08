package cn.hz.ddbm.pc.core.action;


import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/8 0:10
 * @Version 1.0.0
 **/


public class SimpleAction implements Action {
    Action action;

    @Override
    public String beanName() {
        return action.beanName();
    }

    @Override
    public void execute(FlowContext<?> ctx) throws Exception {
        this.action.execute(ctx);
    }
}
