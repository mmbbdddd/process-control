package hz.ddbm.pc.core.domain;

import hz.ddbm.pc.core.config.Coast;
import hz.ddbm.pc.core.support.ChaosAction;
import hz.ddbm.pc.core.utils.InfraUtils;

import java.util.Objects;

public interface Action {
    String beanName();

    void execute(BizContext ctx) throws Exception;

    static Action dsl(String actionDsl, Flow flow) {
        String isChaos = System.getProperty(Coast.IS_CHAOS);
        if (Objects.equals(isChaos, "true")) {
            return buildChaosAction(actionDsl, flow);
        } else {
            return InfraUtils.getBean(actionDsl, Action.class);
        }
    }

    /**
     * 生成不同action的不同混沌实现
     * 1，action执行混沌
     * 2，路由上下文生成混沌
     * @param actionDsl
     * @param flow
     * @return
     */
    static Action buildChaosAction(String actionDsl, Flow flow) {
        //todo
        return new ChaosAction(actionDsl);
    }


}
