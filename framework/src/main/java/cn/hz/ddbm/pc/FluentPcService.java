package cn.hz.ddbm.pc;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.coast.Coasts;

import java.util.HashMap;
import java.util.Map;

public class FluentPcService {
    Map<String, Flow> flows = new HashMap<>();


    public <T extends FlowPayload> void execute(String flowName, T payload, String event) {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payload, "FlowPayload is null");
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Flow           flow = flows.get(flowName);
        FlowContext<T> ctx  = new FlowContext<>(flow, payload, event);
        execute(ctx);
    }

    public void execute(FlowContext<?> ctx) {
        ctx.getFlow().execute(ctx);
    }
}
