package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.example.IDCardDemo;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SendAction implements Action {

    public SendAction() {
    }

    @Override
    public String beanName() {
        return "sendAction";
    }

    @Override
    public Set maybeResult() {
        return null;
    }

    @Override
    public void execute(FlowContext ctx) throws Exception {

    }

}
