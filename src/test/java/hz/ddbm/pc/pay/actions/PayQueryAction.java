package hz.ddbm.pc.pay.actions;

import hz.ddbm.pc.core.domain.Action;
import hz.ddbm.pc.core.domain.BizContext;

public class PayQueryAction implements Action {

    @Override
    public String beanName() {
        return "payQueryAction";
    }

    @Override
    public void execute(BizContext ctx) throws Exception {

    }
}
