package cn.hz.ddbm.pc.core.support;

import cn.hutool.extra.expression.ExpressionUtil;

import java.util.Map;

public class ExpressionEngine {
    public <T> T eval(String expression, Map<String, Object> ctx, Class<T> resultType) {
        return (T) ExpressionUtil.eval(expression, ctx);
    }
}
