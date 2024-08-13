package cn.hz.ddbm.pc.core.router;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.exception.NoRouterResultException;
import cn.hz.ddbm.pc.core.support.ExpressionEngine;
import cn.hz.ddbm.pc.core.utils.InfraUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionRouter implements AnyRouter {
    String routerName;
    /**
     * Node>Expression
     */
    NodeExpression[] nodeExpressionPairs;

    Set<String> toNodes;

    ExpressionEngine expressionEngine;

    public ExpressionRouter(String routerName,NodeExpression... nodeExpressionPairs) {
        this.routerName = routerName;
        this.nodeExpressionPairs = nodeExpressionPairs;
        this.expressionEngine    = InfraUtils.getExpressionEngine();
        this.toNodes             = Arrays.stream(this.nodeExpressionPairs).map(t -> t.to).collect(Collectors.toSet());
    }

    @Override
    public String routerName() {
        return routerName;
    }

    @Override
    public String route(FlowContext<?> ctx) {
        Map<String, Object> exprCtx = ctx.buildExpressionContext();
        String              to      = null;
        for (NodeExpression ne : nodeExpressionPairs) {
            to = ne.to;
            if (expressionEngine.eval(ne.expression, exprCtx, Boolean.class)) {
                return to;
            }
        }
        throw new NoRouterResultException();
    }


    @Override
    public Set<String> toNodes() {
        return this.toNodes;
    }

    public static class NodeExpression {
        String to;
        String expression;

        @Override
        public String toString() {
            return "{" +
                    "to='" + to + '\'' +
                    ", expression='" + expression + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "{" +
                "routerName:" + routerName() +
                ",expr:" + Arrays.toString(nodeExpressionPairs) +
                '}';
    }
}
