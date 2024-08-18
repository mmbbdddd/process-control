package cn.hz.ddbm.pc.core;


import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.StatisticsSupport;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class FlowContext<S extends Enum<S>, T extends FlowPayload<S>> {
    //    入参
    private Serializable   id;
    private T              data;
    private Fsm<S>         flow;
    @Setter
    private Boolean        fluent;
    @Setter
    private Event          event;
    @Setter
    private FlowStatus<S> status;
    @Setter
    private ActionBase<S> executor;
    @Setter
    private Profile       profile;
    @Setter
    private Boolean        mockBean = false;
    @Setter
    private S              nextNode;


    public FlowContext(Fsm<S> flow, T data, String event, Profile profile) {
        Assert.notNull(flow, "flow is null");
        Assert.notNull(data, "data is null");
        Assert.notNull(event, "event is null");
        Assert.notNull(data.getId(), "date.id is null");
        Assert.notNull(data.getStatus(), "date.status is null");
        Assert.notNull(data.getStatus().getFlow(), "date.status.flow is null");
        Assert.notNull(data.getStatus().getNode(), "date.status.node is null");
        this.event   = Event.of(event);
        this.data    = data;
        this.id      = data.getId();
        this.flow    = flow;
        this.status  = data.getStatus();
        this.fluent  = true;
        this.profile = profile;
    }


    public void metricsNode(FlowContext<S, ?> ctx) {
        String            windows        = String.format("%s:%s:%s:%s", ctx.getFlow().getName(), ctx.getId(), ctx.getStatus().getNode(), Coasts.NODE_RETRY);
        StatisticsSupport metricsWindows = InfraUtils.getMetricsTemplate();
        metricsWindows.increment(windows);
    }

    /**
     * 确保上下文状态一致
     * 1，status ==> entity
     */
    public void syncPayLoad() {
        data.setStatus(status);
    }

    public Map<String, Object> buildExpressionContext() {
        return null;
    }
}
