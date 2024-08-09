package cn.hz.ddbm.pc.core.router;

import cn.hz.ddbm.pc.core.Event;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.exception.NoRouterResultException;
import cn.hz.ddbm.pc.core.support.ExpressionEngine;
import cn.hz.ddbm.pc.core.utils.InfraUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ExpressionAnyRouter implements AnyRouter {
    /**
     * Node>Expression
     */
    Map<String, String> expressions;

    ExpressionEngine expressionEngine;

    public ExpressionAnyRouter(Map<String, String> expressions) {
        this.expressions      = expressions;
        this.expressionEngine = InfraUtils.getExpressionEngine();
    }

    @Override
    public String routerName() {
        return null;
    }

    @Override
    public String route(FlowContext<?> ctx) {
        Map<String, Object> eCtx = ctx.buildExpressionContext();
        String              to   = null;
        for (Map.Entry<String, String> entry : expressions.entrySet()) {
            to = entry.getKey();
            String expr = entry.getValue();
            if (expressionEngine.eval(expr, eCtx, Boolean.class)) {
                return to;
            }
        }
        throw new NoRouterResultException();
    }

    @Override
    public String failover(String preNode, FlowContext<?> ctx) {
        return null;
    }

    @Override
    public Set<String> toNodes() {
        return expressions.keySet();
    }


}
