package cn.hz.ddbm.pc.container.chaos;

import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.profile.chaos.ChaosRule;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 混沌发生器。具体发生规则参见ChaosRule
 */
public class ChaosHandler {

    ChaosPcService chaosPcService;

    public ChaosHandler(ChaosPcService chaosPcService) {
        this.chaosPcService = chaosPcService;
    }

    /**
     * 判断当前执行对象有没有触发混沌规则表（chaosRule）
     *
     * @param proxy
     * @param method
     * @param args
     * @throws Throwable
     */
    public void handle(Object proxy, Method method, Object[] args) throws Throwable {
        List<ChaosRule> rules = chaosPcService.chaosRules();
        if (null != rules) {
            for (ChaosRule rule : rules) {
                if (rule.match(proxy, method, args) && rule.probabilityIsTrue()) {
                    rule.raiseException();
                }
            }
        }
    }


}
