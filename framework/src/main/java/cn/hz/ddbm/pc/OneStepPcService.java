package cn.hz.ddbm.pc;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;

import java.util.HashMap;
import java.util.Map;

public class OneStepPcService extends FluentPcService {
    Map<String, Flow> flows = new HashMap<>();

    public void oneStep(FlowContext<?> ctx) {
        Flow    flow      = ctx.getFlow();
        Boolean rawFluent = flow.getFluent();
        flow.setFluent(false);
        execute(ctx);
        flow.setFluent(rawFluent);
    }

}
