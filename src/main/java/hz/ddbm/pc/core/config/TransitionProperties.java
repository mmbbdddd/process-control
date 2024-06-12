package hz.ddbm.pc.core.config;

import lombok.Getter;

@Getter
public class TransitionProperties {
    String event;
    String action;
    String router;
    String failToNode;
    String saga;

    public TransitionProperties(String event, String action, String router, String failToNode, String saga) {
        this.event = event;
        this.action = action;
        this.router = router;
        this.failToNode = failToNode;
        this.saga = saga;
    }
}
