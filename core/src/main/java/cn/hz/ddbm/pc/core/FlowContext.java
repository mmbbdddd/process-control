package cn.hz.ddbm.pc.core;


import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.MetricsTemplate;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class FlowContext<T extends FlowPayload> {
    //    入参
    private Serializable id;
    private T            data;
    private Flow         flow;
    @Setter
    private Boolean      fluent;
    @Setter
    private Event        event;
    @Setter
    private FlowStatus   status;
    @Setter
    private AtomExecutor atomExecutor;


    public FlowContext(Flow flow, T data, String event) {
        Assert.notNull(flow, "flow is null");
        Assert.notNull(data, "data is null");
        Assert.notNull(event, "event is null");
        Assert.notNull(data.getId(), "date.id is null");
        Assert.notNull(data.getStatus(), "date.status is null");
        Assert.notNull(data.getStatus().getFlow(), "date.status.flow is null");
        Assert.notNull(data.getStatus().getNode(), "date.status.node is null");
        this.event  = Event.of(event);
        this.data   = data;
        this.id     = data.getId();
        this.flow   = flow;
        this.status = data.getStatus();
        this.fluent = true;
    }


    public void metricsNode(FlowContext<?> ctx) {
        String          windows        = String.format("%s:%s:%s:%s", ctx.getFlow().getName(), ctx.getId(), ctx.getStatus().getNode(), Coasts.NODE_RETRY);
        MetricsTemplate metricsWindows = InfraUtils.getMetricsTemplate();
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
