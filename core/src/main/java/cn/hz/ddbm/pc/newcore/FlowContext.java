package cn.hz.ddbm.pc.newcore;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hz.ddbm.pc.newcore.config.Coast;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class FlowContext<S extends State> {
    public BaseFlow<S>         flow;
    public String              id;
    public String              uuid;
    public S                   state;
    public Payload<S>          payload;
    public String              event;
    public Map<String, Object> session;
    public Boolean             fluent;

    public Object action;
    public String errorMessage;

    public FlowContext(BaseFlow<S> flow, Payload<S> payload) {
        this(flow, payload, null, null, null);
    }

    public FlowContext(BaseFlow<S> flow, Payload<S> payload, String event, Map<String, Object> session, Boolean fluent) {
        Assert.notNull(flow, "flow is null");
        Assert.notNull(payload, "payload is null");
        this.flow    = flow;
        this.payload = payload;
        this.id      = payload.getId();
        this.uuid    = UUID.randomUUID().toString(true);
        this.state   = payload.getState();
        this.event   = null == event ? Coast.EVENT_DEFAULT : event;
        this.session = null == session ? new HashMap<>() : session;
        this.fluent  = null == fluent || fluent;
    }
}
