package cn.hz.ddbm.pc.core.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class QueryAction implements Action {

    @Override
    public String beanName() {
        return "queryAction";
    }

    @Override
    public Set maybeResult() {
        return null;
    }

    @Override
    public void execute(FlowContext ctx) throws Exception {
    }

}
