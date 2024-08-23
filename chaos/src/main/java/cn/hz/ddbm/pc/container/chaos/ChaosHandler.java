package cn.hz.ddbm.pc.container.chaos;

import cn.hutool.core.lang.Pair;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.support.Locker;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.RandomUitl;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.profile.chaos.ChaosRule;
import cn.hz.ddbm.pc.profile.chaos.ChaosTarget;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 混沌发生器。具体发生规则参见ChaosRule
 */
public class ChaosHandler {

    List<ChaosRule> beanRules;
    List<ChaosRule> actionRules;

    public ChaosHandler(ChaosPcService chaosPcService) {
        List<ChaosRule> chaosRules = chaosPcService.chaosRules();
        this.beanRules   = chaosRules.stream()
                                     .filter(r -> r.equals(ChaosTarget.LOCK) || r.equals(ChaosTarget.SESSION) || r.equals(ChaosTarget.STATUS))
                                     .collect(Collectors.toList());
        this.actionRules = chaosRules.stream()
                                     .filter(r -> r.equals(ChaosTarget.ACTION))
                                     .collect(Collectors.toList());
    }

    public void action(FsmContext ctx) {
        Profile profile = ctx.getProfile();
        if (ctx.getIsChaos()) {
            BaseProcessor actionBase = ctx.getExecutor();
            Enum          nextNode   = null;
            if (actionBase.getFsmRecord().getType().equals(Fsm.TransitionType.TO)) {
                nextNode = actionBase.getFsmRecord().getTo();
            } else {
                Enum                    from        = actionBase.getFsmRecord().getFrom();
                String                  event       = actionBase.getFsmRecord().getEvent();
                Set<Pair<Enum, Double>> statusRadio = (Set<Pair<Enum, Double>>) profile.getMaybeResults().get(from, event);
                String                  key         = String.format("%s_%s", from.name(), event);
                nextNode = RandomUitl.selectByWeight(key, statusRadio);
            }
            ctx.setNextNode(nextNode);
        }
    }

    /**
     * 判断当前执行对象有没有触发混沌规则表（chaosRule）
     *
     * @param proxy
     * @param method
     * @param args
     * @throws Throwable
     */
    public <S extends Enum<S>> void handle(Object proxy, Method method, Object[] args) throws Throwable {
        ChaosTarget targetType = getTargetType(proxy);

        if (null != beanRules) {
            for (ChaosRule rule : beanRules) {
                if (rule.match(targetType, proxy, method, args) && rule.probabilityIsTrue()) {
                    rule.raiseException();
                }
            }
        }

    }

    private ChaosTarget getTargetType(Object proxy) {
        if (proxy instanceof Action) {
            return ChaosTarget.ACTION;
        }
        if (proxy instanceof SessionManager) {
            return ChaosTarget.SESSION;
        }
        if (proxy instanceof StatusManager) {
            return ChaosTarget.STATUS;
        }
        if (proxy instanceof Locker) {
            return ChaosTarget.LOCK;
        }
        return null;
    }


}
