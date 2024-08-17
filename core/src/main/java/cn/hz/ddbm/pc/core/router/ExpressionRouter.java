package cn.hz.ddbm.pc.core.router;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.State;
import cn.hz.ddbm.pc.core.utils.ExpressionEngineUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionRouter implements AnyRouter, State.Instant {
    String           routerName;
    /**
     * Node>Expression
     */
    NodeExpression[] nodeExpressionPairs;

    Set<String> toNodes;


    public ExpressionRouter(String routerName, NodeExpression... nodeExpressionPairs) {
        this.routerName          = routerName;
        this.nodeExpressionPairs = nodeExpressionPairs;
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
            if (ExpressionEngineUtils.eval(ne.expression, exprCtx, Boolean.class)) {
                return to;
            }
        }
        throw new RuntimeException("no router result for status:" + ctx.getStatus().getNode());
    }


    @Override
    public String status() {
        return routerName;
    }

    @Override
    public String toString() {
        return "{" +
                "routerName:" + routerName() +
                ",expr:" + Arrays.toString(nodeExpressionPairs) +
                '}';
    }

    public static class NodeExpression {
        String to;
        String expression;

        public NodeExpression(String to, String expression) {
            this.to         = to;
            this.expression = expression;
        }

        @Override
        public String toString() {
            return "{" +
                    "to='" + to + '\'' +
                    ", expression='" + expression + '\'' +
                    '}';
        }
    }
}
