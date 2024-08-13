package cn.hz.ddbm.pc;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.SessionException;
import cn.hz.ddbm.pc.core.exception.StatusException;

import java.util.HashMap;
import java.util.Map;

public class SingleStepPcService extends PcService {
    Map<String, Flow> flows = new HashMap<>();

    public <T extends FlowPayload> void oneStep(String flowName, T payload, String event) throws StatusException, SessionException {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payload, "FlowPayload is null");
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Flow           flow = flows.get(flowName);
        FlowContext<T> ctx  = new FlowContext<>(flow, payload, event);
        oneStep(ctx);
    }

    public void oneStep(FlowContext<?> ctx) throws StatusException, SessionException {
        Flow    flow      = ctx.getFlow();
        Boolean rawFluent = flow.getFluent();
        flow.setFluent(false);
        flow.execute(ctx);
        flow.setFluent(rawFluent);
    }

}
