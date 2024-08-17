package cn.hz.ddbm.pc.core;

import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.router.ToRouter;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ActionRouter implements State.Instant {
    String status;
    String action;
    Router router;

    public ActionRouter(String action, Router router) {
        this.action = action;
        this.router = router;
        this.status = null;
        if (router instanceof ToRouter) {
            this.status = ((ToRouter) router).getTo();
        } else if (router instanceof ExpressionRouter) {
            this.status = ((ExpressionRouter) router).status();
        }
    }

    @Override
    public String status() {
        return status;
    }

    public Action action(FlowContext<?> ctx) {
        return Action.of(action, ctx);
    }

    /**
     * event>toNode
     *
     * @return
     */
    public Set<Fsm.FsmRecord> fsmRecords(String from, Event event) {
        ActionRouter self = this;
        return new HashSet<Fsm.FsmRecord>() {{
            if (router instanceof ToRouter) {
                add(new Fsm.FsmRecord(from, event, self));
            }
            if (router instanceof ExpressionRouter) {
                String status = ((ExpressionRouter) router).status();
                add(new Fsm.FsmRecord(from, event, self));
                add(new Fsm.FsmRecord(status, event, new ActionRouter(Coasts.NONE, router)));
            }
        }};
    }


}
