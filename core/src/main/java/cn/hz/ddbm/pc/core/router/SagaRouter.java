package cn.hz.ddbm.pc.core.router;

import cn.hz.ddbm.pc.core.FlowContext;

import java.util.Map;

public class SagaRouter extends ExpressionRouter{

    private final String failover;

    public SagaRouter(String failover, Map<String, String> expressions) {
        super(expressions);
        this.failover = failover;
    }

    @Override
    public String failover(String beforeNode, FlowContext<?> ctx) {
        return this.failover;
    }
}
