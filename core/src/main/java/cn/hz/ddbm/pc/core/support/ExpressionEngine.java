package cn.hz.ddbm.pc.core.support;

import java.util.Map;

public interface ExpressionEngine {
    <T> T eval(String expression, Map<String, Object> ctx, Class<T> resultType);
}
