package cn.hz.ddbm.pc;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowPayload;

import java.util.HashMap;
import java.util.Map;

public class FluentPcService {
    Map<String, Flow> flows = new HashMap<>();


    public <T extends FlowPayload> void execute(String flow, T data, String event) {

    }

    public void execute(FlowContext<?> ctx) {
        ctx.getFlow().execute(ctx);
    }
}
