package cn.hz.ddbm.pc.core;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ActionRouter implements Step.Instant {
    String status;
    Action action;
    Router router;

    public ActionRouter(String from, String event, Action action, Router router) {
        this.action = action;
        this.router = router;
        this.status = String.format("%s||%sInstantStatus", from, event);
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
    public Map<Event, String> eventToNodes() {
        return new HashMap<Event, String>() {{
            router.toNodes().forEach(toNode -> {
                put(Event.of(Event.Type.INSTANT_TO_EVENT, status()), toNode);
            });
        }};
    }
}
