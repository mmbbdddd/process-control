package cn.hz.ddbm.pc.core.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import org.springframework.stereotype.Component;

@Component
public class QueryAction implements Action {

    @Override
    public String beanName() {
        return "queryAction";
    }

    @Override
    public Enum execute(FlowContext ctx) throws Exception {
        return null;
    }

}
