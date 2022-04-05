package io.ddbm.pc;

import io.ddbm.pc.utils.SpelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 路由定义
 */
public abstract class Router {
    Logger logger = LoggerFactory.getLogger(getClass());

    public enum Type {
        NAME, EXPRESSION
    }


    public abstract _Node routeTo(FlowContext ctx) throws RouterException;


}

class NameRouter extends Router {
    String targetNode;

    public NameRouter(String targetNode) {
        this.targetNode = targetNode;
    }

    @Override
    public _Node routeTo(FlowContext ctx) throws RouterException {
        return ctx.getFlow().getNode(targetNode);
    }

}

class ExpressionRouter extends Router {
    String              routerName;
    Map<String, String> expressions;

    public ExpressionRouter(String routerName, LinkedHashMap<String, String> expressions) {
        this.routerName  = routerName;
        this.expressions = expressions;
    }

    @Override
    public _Node routeTo(FlowContext ctx) throws RouterException {
        for (Map.Entry<String, String> entry : expressions.entrySet()) {
            String  node       = entry.getKey();
            String  expression = entry.getValue();
            Boolean result     = null;
            try {
                result = SpelUtils.eval(expression, asMap(ctx));
                if (result) {
                    _Node targetNode = ctx.flow.getNode(node);
                    if (null != targetNode) {
                        return targetNode;
                    }
                }
            } catch (Exception e) {
                throw new RouterException("路由表达式执行错误", ctx, e);
            }
        }
        throw new RouterException("找不到匹配的路由表达式", ctx);
    }


    private Map<String, Object> asMap(FlowContext ctx) {
        Map<String, Object> el = new HashMap<>();
        el.put("data", ctx.getRecord());
        el.put("id", ctx.id);
        el.put("flow", ctx.getFlow());
        el.put("node", ctx.getNode());
        el.put("result", ctx.getActionResult());
        el.put("error", ctx.getActionException());
        //设置action的结果  TODO
        return el;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionRouter that = (ExpressionRouter) o;
        return Objects.equals(routerName, that.routerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routerName);
    }
}