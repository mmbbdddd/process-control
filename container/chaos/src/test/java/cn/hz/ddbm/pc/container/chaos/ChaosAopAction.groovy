package cn.hz.ddbm.pc.container.chaos;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import org.springframework.stereotype.Component;

@Component
public class ChaosAopAction implements Action {
    @Override
    public String beanName() {
        return "null";
    }

    @Override
    public void execute(FlowContext<?> ctx) throws Exception {
        System.out.println("xxxx");
    }
}
