package cn.hz.ddbm.pc.core;

import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.router.ToRouter;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ActionRouter implements State.Instant {
    String from;
    String status;
    Action action;
    Router router;

    public ActionRouter(String from, Action action, Router router) {
        this.action = action;
        this.router = router;
        this.from   = from;
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

    /**
     * event>toNode
     *
     * @return
     */
    public Set<Flow.FsmRecord> fsmRecords(Event event) {
        return new HashSet<Flow.FsmRecord>() {{
            if (router instanceof ToRouter) {
                add(new Flow.FsmRecord(from, event, new ActionRouter(from, action, router)));
            }
            if (router instanceof ExpressionRouter) {
                String status = ((ExpressionRouter) router).status();
                add(new Flow.FsmRecord(from, event, new ActionRouter(from, action, new ToRouter(from, status))));
                add(new Flow.FsmRecord(status, event, new ActionRouter(status, Coasts.NONE_ACTION, router)));
            }
        }};
    }


}
