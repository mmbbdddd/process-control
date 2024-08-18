package cn.hz.ddbm.pc.example.actions;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.SimpleAction;
import cn.hz.ddbm.pc.example.IDCardState;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidateAndNotifyUserAction implements SimpleAction<IDCardState> {
    @Override
    public String beanName() {
        return "validateAndNotifyUserAction";
    }

    @Override
    public void execute(FlowContext<IDCardState, ?> ctx) throws Exception {

    }


}
