package hz.ddbm.pc.core.utils;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class ElUtils {
    static SpelExpressionParser parser = new SpelExpressionParser();

    public static Boolean evalBoolean(String expression, Map<String, Object> ctx) {
        return eval(expression, ctx, Boolean.class);
    }

    public static Object eval(String expression, Map<String, Object> args) {
        return eval(expression, args, Object.class);
    }


    public static <T> T eval(String expression, Map<String, Object> args, Class<T> result) {
        Assert.notNull(expression, "expression is null");
        args = null == args ? new HashMap<>() : args;
        StandardEvaluationContext ectx = new StandardEvaluationContext(args);
        ectx.addPropertyAccessor(new MapAccessor());
        args.forEach(ectx::setVariable);
        return parser.parseExpression(expression).getValue(ectx, result);
    }


}
