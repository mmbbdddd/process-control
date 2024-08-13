package cn.hz.ddbm.pc;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.SessionException;
import cn.hz.ddbm.pc.core.exception.StatusException;
import cn.hz.ddbm.pc.core.utils.InfraUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PcService {
    Map<String, Flow> flows = new HashMap<>();


    public <T extends FlowPayload> void batchExecute(String flowName, List<T> payloads, String event) throws StatusException, SessionException {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payloads, "FlowPayload is null");
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Flow flow = flows.get(flowName);

        for (T payload : payloads) {
            FlowContext<T> ctx = new FlowContext<>(flow, payload, event);
            execute(ctx);
        }
    }

    public <T extends FlowPayload> void execute(String flowName, T payload, String event) throws StatusException, SessionException {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payload, "FlowPayload is null");
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Flow           flow = flows.get(flowName);
        FlowContext<T> ctx  = new FlowContext<>(flow, payload, event);
        execute(ctx);
    }

    public <T extends FlowPayload> void execute(FlowContext<T> ctx) throws StatusException, SessionException {
        if (Boolean.FALSE.equals(tryLock(ctx))) {
            return;
        }
        try {
            ctx.getFlow().execute(ctx);
        } finally {
            releaseLock(ctx);
        }
    }

    void addFlow(Flow flow) {
        this.flows.put(flow.getName(), flow);
    }


    private void releaseLock(FlowContext<?> ctx) {
        InfraUtils.releaseLock(String.format("%s:%s:%s", InfraUtils.getDomain(), ctx.getFlow().getName(), ctx.getId()));
    }

    private Boolean tryLock(FlowContext<?> ctx) {
        return InfraUtils.tryLock(String.format("%s:%s:%s", InfraUtils.getDomain(), ctx.getFlow().getName(), ctx.getId()), 10);
    }

}
