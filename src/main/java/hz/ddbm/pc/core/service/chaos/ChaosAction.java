package hz.ddbm.pc.core.service.chaos;

import hz.ddbm.pc.core.fsm.core.Action;
import hz.ddbm.pc.core.fsm.core.BizContext;

public class ChaosAction implements Action {
    String actionDsl;

    public ChaosAction(String actionDsl) {
        this.actionDsl = actionDsl;
    }

    @Override
    public String beanName() {
        return "chaosAction#" + actionDsl;
    }

    @Override
    public void execute(BizContext ctx) throws Exception {
        Double r1 = Math.random();
        Double r2 = Math.random();
        if (r1 > 0.1) {
            throw new Exception("混沌Exception");
        }
        if (r2 > 0.1) {
            throw new RuntimeException("混沌RuntimeException");
        }
    }
}
