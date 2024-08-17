package cn.hz.ddbm.pc.profile.chaos;

import cn.hz.ddbm.pc.core.utils.ExpressionEngineUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 混沌规则表
 */
public class ChaosRule {
    /**
     * 触发条件，el表达式。
     * 表达式的值有三类
     * obj：执行对象
     * method：方法
     * 参数：分别以args1，args2，args3.。。命名
     */
    String                           expression;
    //触发概率
    double                           probability;
    //触发后，跑出的异常类型（随便选一个）
    List<Class<? extends Throwable>> errorTypes;

    public ChaosRule(String expression, double probability, List<Class<? extends Throwable>> errorTypes) {
        this.expression  = expression;
        this.probability = probability;
        this.errorTypes  = errorTypes;
    }

    public boolean match(Object proxy, Method method, Object[] args) {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("obj", proxy);
        ctx.put("method", method.getName());
        for (int idx = 0; idx < args.length; idx++) {
            ctx.put("arg" + (idx + 1), args[idx]);
        }
        return ExpressionEngineUtils.eval(expression, ctx, Boolean.class);
    }

    public void raiseException() throws Throwable {
        int choice = Double.valueOf(Math.random() * errorTypes.size()).intValue();
        errorTypes.get(choice).newInstance();
    }

    public boolean probabilityIsTrue() {
        return Math.random() < probability;
    }
}
