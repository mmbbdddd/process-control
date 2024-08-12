package cn.hz.ddbm.pc.core;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ActionRouter implements Step.Instant {
    Action         action;
    Router<String> router;

    public ActionRouter(Action action, Router<String> router) {
        this.action = action;
        this.router = router;
    }

    @Override
    public String status() {
        return "";
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
