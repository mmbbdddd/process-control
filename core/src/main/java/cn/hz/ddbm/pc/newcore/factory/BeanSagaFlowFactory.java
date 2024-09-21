package cn.hz.ddbm.pc.newcore.factory;

import cn.hz.ddbm.pc.newcore.saga.SagaFlow;
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
public class BeanSagaFlowFactory implements ApplicationContextAware, FlowFactory<SagaFlow> {
    private ApplicationContext ctx;

    @Override
    public Map<String, SagaFlow> getFlows() {
        List<SagaFlow> flows = ctx.getBeansOfType(SAGA.class).entrySet().stream().map(t -> {
            try {
                SagaFlow flow = t.getValue().build();
                flow.validate();
                return flow;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return flows.stream().collect(Collectors.toMap(
                SagaFlow::name,
                t -> t
        ));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
