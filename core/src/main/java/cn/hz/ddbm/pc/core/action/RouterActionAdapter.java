package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;

/**
 * 适配路由为Action
 * 设计文档：
 */
public class RouterActionAdapter implements Action {
    @Override
    public String beanName() {
        return "";
    }

    @Override
    public void execute(FlowContext<?> ctx) throws Exception {

    }
}
