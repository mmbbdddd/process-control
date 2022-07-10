package io.ddbm.pc.utils;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

public class SpelUtils {
    static ExpressionParser parser = new SpelExpressionParser();

    public static Boolean eval(String expression, Map<String, Object> asMap) {
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        asMap.forEach(ctx::setVariable);
        return parser.parseExpression(expression).getValue(ctx, Boolean.class);
    }
//    public static Boolean eval(String expression, Map<String, Object> asMap) throws RouterException {
//        try {
//            return (Boolean) Ognl.getValue(expression, asMap, null, Boolean.class);
//        } catch (OgnlException e) {
//            throw new RouterException("");
//        }
//    }
}
