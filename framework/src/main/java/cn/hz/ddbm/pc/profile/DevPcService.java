package cn.hz.ddbm.pc.profile;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.wrap.ActionException;
import cn.hz.ddbm.pc.core.exception.wrap.RouterException;

public class DevPcService extends PcService {
    public <S extends Enum<S>,T extends FlowPayload<S>> void oneStep(String flowName, T payload, String event) throws ActionException, RouterException, FsmEndException {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payload, "FlowPayload is null");
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Fsm<S>            flow = getFlow(flowName);
        FlowContext<S,T> ctx  = new FlowContext<>(flow, payload, event, Profile.devOf());
        oneStep(ctx);
    }

    public void oneStep(FlowContext<?,?> ctx) throws RouterException, ActionException, FsmEndException {
        Fsm     flow      = ctx.getFlow();
        Boolean rawFluent = ctx.getFluent();
        ctx.setFluent(false);
        flow.execute(ctx);
        ctx.setFluent(rawFluent);
    }

}
