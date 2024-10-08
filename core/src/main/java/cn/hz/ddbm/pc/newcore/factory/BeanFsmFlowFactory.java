package cn.hz.ddbm.pc.newcore.factory;

import cn.hz.ddbm.pc.newcore.BaseFlow;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlow;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定义的流程的定义方式
 * 有xml，json，buider等方式。
 */
public class BeanFsmFlowFactory implements ApplicationContextAware, FlowFactory<FsmFlow> {
    private ApplicationContext ctx;

    @Override
    public Map<String, FsmFlow> getFlows() {
        List<FsmFlow> flows = ctx.getBeansOfType(FSM.class).entrySet().stream().map(t -> {
            try {
                FsmFlow flow = t.getValue().build();
                flow.validate();
                return flow;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return flows.stream().collect(Collectors.toMap(
                BaseFlow::name,
                t -> t
        ));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
