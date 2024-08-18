package cn.hz.ddbm.pc.core.router;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.utils.ExpressionEngineUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionRouter<S extends Enum<S>> implements AnyRouter<S> {
    String              routerName;
    /**
     * Node>Expression
     */
    NodeExpression<S>[] nodeExpressionPairs;

    Set<S> toNodes;


    public ExpressionRouter(String routerName, NodeExpression<S>... nodeExpressionPairs) {
        this.routerName          = routerName;
        this.nodeExpressionPairs = nodeExpressionPairs;
        this.toNodes             = Arrays.stream(this.nodeExpressionPairs).map(t -> t.to).collect(Collectors.toSet());
    }

    @Override
    public String routerName() {
        return routerName;
    }

    @Override
    public S route(FlowContext<S, FlowPayload<S>> ctx) {
        Map<String, Object> exprCtx = ctx.buildExpressionContext();
        S                   to      = null;
        for (NodeExpression<S> ne : nodeExpressionPairs) {
            to = ne.to;
            if (ExpressionEngineUtils.eval(ne.expression, exprCtx, Boolean.class)) {
                return to;
            }
        }
        throw new RuntimeException("no router result for status:" + ctx.getStatus().getNode());
    }


    @Override
    public String toString() {
        return "{" +
                "routerName:" + routerName() +
                ",expr:" + Arrays.toString(nodeExpressionPairs) +
                '}';
    }

    public static class NodeExpression<S extends Enum<S>> {
        S      to;
        String expression;

        public NodeExpression(S to, String expression) {
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
