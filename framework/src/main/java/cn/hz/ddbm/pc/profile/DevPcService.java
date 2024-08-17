package cn.hz.ddbm.pc.profile;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.ActionException;
import cn.hz.ddbm.pc.core.exception.RouterException;

public class DevPcService extends PcService {
    public <T extends FlowPayload> void oneStep(String flowName, T payload, String event) throws ActionException, RouterException {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payload, "FlowPayload is null");
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Flow           flow = getFlow(flowName);
        FlowContext<T> ctx  = new FlowContext<>(flow, payload, event, Profile.devOf());
        oneStep(ctx);
    }

    public void oneStep(FlowContext<?> ctx) throws RouterException, ActionException {
        Flow    flow      = ctx.getFlow();
        Boolean rawFluent = ctx.getFluent();
        ctx.setFluent(false);
        flow.execute(ctx);
        ctx.setFluent(rawFluent);
    }

}
