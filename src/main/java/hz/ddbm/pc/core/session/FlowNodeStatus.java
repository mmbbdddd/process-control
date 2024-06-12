package hz.ddbm.pc.core.session;

import hz.ddbm.pc.core.domain.Flow;
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
