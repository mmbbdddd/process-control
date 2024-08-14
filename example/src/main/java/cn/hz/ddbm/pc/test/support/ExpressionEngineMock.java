package cn.hz.ddbm.pc.test.support;

import cn.hz.ddbm.pc.core.support.ExpressionEngine;
import org.mvel2.MVEL;

import java.util.Map;

public class ExpressionEngineMock implements ExpressionEngine {
    @Override
    public <T> T eval(String expression, Map<String, Object> ctx, Class<T> resultType) {
        try {
            return MVEL.eval(expression, ctx, resultType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
