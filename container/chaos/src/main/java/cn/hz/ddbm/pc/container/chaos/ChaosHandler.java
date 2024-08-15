package cn.hz.ddbm.pc.container.chaos;

import cn.hz.ddbm.pc.container.ChaosContainer;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.profile.chaos.ChaosRule;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;


public class ChaosHandler {
    List<ChaosRule> chaosRules;
    @Resource
    ChaosContainer container;

    /**
     * 判断当前执行对象有没有触发混沌规则表（chaosRule）
     *
     * @param proxy
     * @param method
     * @param args
     * @throws Throwable
     */
    public void handle(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("chaos");
        if (null != chaosRules) {
            for (ChaosRule rule : chaosRules) {
                if (rule.match(proxy, method, args) && rule.probabilityIsTrue()) {
                    rule.raiseException();
                }
            }
        }
    }

    @PostConstruct
    public void init() {
        this.chaosRules = container.getBean(ChaosPcService.class).chaosRules();
    }


}
