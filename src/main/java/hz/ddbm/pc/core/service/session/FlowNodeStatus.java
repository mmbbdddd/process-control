package hz.ddbm.pc.core.service.session;

import hz.ddbm.pc.core.fsm.core.Flow;
import lombok.Getter;

@Getter
public class FlowNodeStatus {
    Flow.STAUS flow;
    String     node;

    public FlowNodeStatus() {

    }
    public FlowNodeStatus(Flow.STAUS flow, String node) {
        this.flow = flow;
        this.node = node;
    }
}
