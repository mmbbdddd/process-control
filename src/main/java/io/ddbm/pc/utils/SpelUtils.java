package io.ddbm.pc.utils;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

public class SpelUtils {
    static ExpressionParser parser = new SpelExpressionParser();

    public static Object eval(String expression, Map<String, Object> asMap) {
        StandardEvaluationContext ctx = new StandardEvaluationContext(asMap);
        return parser.parseExpression(expression).getValue(ctx);
    }
}
