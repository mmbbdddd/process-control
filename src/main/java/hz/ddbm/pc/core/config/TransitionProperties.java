package hz.ddbm.pc.core.config;

import lombok.Getter;
import lombok.Setter;

@Getter
public class TransitionProperties {
    String event;
    String action;
    String router;
    @Setter
    String failToNode;
    @Setter
    String saga;
    @Setter
    String retryTimes;

    public TransitionProperties(String event, String action, String router) {
        this.event = event;
        this.action = action;
        this.router = router;
    }
}
