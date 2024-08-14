package cn.hz.ddbm.pc.core;

import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.router.ToRouter;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ActionRouter implements State.Instant {
    String from;
    String to;
    Action action;
    Router router;

    public ActionRouter(String from, Action action, Router router) {
        this.action = action;
        this.router = router;
        this.from   = null;
        this.to     = null;
        if (router instanceof ToRouter) {
            this.to = ((ToRouter) router).getTo();
        } else if (router instanceof ExpressionRouter) {
            this.to = ((ExpressionRouter) router).status();
        }
    }

    @Override
    public String status() {
        return to;
    }

    /**
     * event>toNode
     *
     * @return
     */
    public Map<Event, String> eventToNodes() {
        return new HashMap<Event, String>() {{
            if (router instanceof ExpressionRouter) {
                router.toNodes().forEach(toNode -> {
                    put(Event.expressionRouterOf(status(), toNode), toNode);
                });
            } else if (router instanceof ToRouter) {
                router.toNodes().forEach(toNode -> {
                    put(Event.toRouterOf(from, toNode), toNode);
                });
            }
        }};
    }


}
