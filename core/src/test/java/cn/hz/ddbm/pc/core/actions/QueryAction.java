package cn.hz.ddbm.pc.core.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class QueryAction implements SimpleAction {

    @Override
    public String beanName() {
        return "queryAction";
    }



    @Override
    public void execute(FlowContext ctx) throws Exception {
    }

}
