package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.session.RedisSession;
import io.ddbm.pc.utils.SpringUtils;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Getter
public class FlowContext {
    //    当前工作流数据
    private final FlowRequest request;
    private final Flow        flow;
    private       TaskNode    node;
    private       Event       event;
    private       Session     session;

    public FlowContext(Flow flow, FlowRequest request, String event) throws InterruptException {
        event = StringUtils.isEmpty(event) ? Coast.DEFAULT_EVENT : event;
        Assert.notNull(flow, "flow is null");
        Assert.notNull(request, "request is null");
        this.flow    = flow;
        this.request = request;
        this.node    = flow.nodeOf(request.getStatus());
        this.request.setStatus(node.getName());
        this.event = this.node.getEvent(event);
        if (request.getSession() == null) {
            this.session = SpringUtils.getBean(RedisSession.class);
        } else {
            this.session = request.getSession();
        }
    }

}
