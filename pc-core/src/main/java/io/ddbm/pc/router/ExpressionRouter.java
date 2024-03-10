package io.ddbm.pc.router;

import io.ddbm.pc.Router;
import io.ddbm.pc.exception.RouterException;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;
import io.ddbm.pc.utils.SpelUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ExpressionRouter implements Router {
    @Getter
    String routerName;

    @Getter
    List<Pair<String, String>> expressions;

    public ExpressionRouter(String routerName) {
        this.routerName = routerName;
        this.expressions = new ArrayList<>();
    }

    @Override
    public String route(FlowContext ctx)
        throws RouterException {
        String targetNode = null;
        //        1,构建表达式上下文
        Map<String, Object> expressionContext = new HashMap<>();

        //        2，执行路由表达式
        Map<String, Pair<Boolean, Object>> routerExpressionResult = new HashMap<>();
        for (Pair<String, String> expression : expressions) {
            String test = expression.getKey();
            String to = expression.getValue();
            try {
                Boolean expressionResult = SpelUtils.eval(test, expressionContext);
                routerExpressionResult.put(test, Pair.of(expressionResult, to));
            } catch (Throwable e) {
                routerExpressionResult.put(test, Pair.of(null, e));
            }
        }
        //3，返回路由 TODO 优化
        //3.1 如果有路由结果，先返回（有异常打印，不抛出）
        //3.2 如果无路由，抛出异常（流程暂停），打印异常
        if (existTrueResult(routerExpressionResult)) {
            return (String)routerExpressionResult.values().stream().filter(
                result -> Objects.equals(Boolean.TRUE, result.getKey())).findFirst().get().getValue();
        } else {
            throw new RouterException(routerName, routerName + "路由器无结果");
        }
    }

    @Override
    public String name() {
        return routerName;
    }

    private boolean existTrueResult(Map<String, Pair<Boolean, Object>> routerExpressionResults) {
        return routerExpressionResults.values().stream().anyMatch(
            result -> Objects.equals(Boolean.TRUE, result.getKey()));
    }
}
