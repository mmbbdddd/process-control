package cn.hz.ddbm.pc.newcore.chaos;

import cn.hutool.core.lang.Pair;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.actions.RemoteSagaAction;
import cn.hz.ddbm.pc.newcore.utils.RandomUitl;
import lombok.Setter;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * 混沌发生器
 * 1，根据规则混沌
 * 2，根据规则生成交易结果
 * <p>
 * 规则格式
 * action：ChaosTargetType值。
 * type
 */
public class ChaosHandler {
    @Setter
    ChaosConfig chaosConfig;

    private ChaosConfig getChaosConfig() {
        return null == chaosConfig ? ChaosConfig.defaultOf() : chaosConfig;
    }

    /**
     * 业务逻辑混沌注入
     *
     * @throws Exception
     */
    public void infraChaos() throws Exception {
        ChaosRule rule = RandomUitl.selectByWeight("infraChaosRule", getChaosConfig().infraChaosRule());
        if (rule.isException()) {
            rule.raiseException();
        }
    }


    public static SagaAction.QueryResult sagaRemoteResult() {
        Set<Pair<RemoteSagaAction.QueryResult, Double>> results = new HashSet<>();
        results.add(Pair.of(SagaAction.QueryResult.none, 0.1));
        results.add(Pair.of(SagaAction.QueryResult.exception, 0.1));
        results.add(Pair.of(SagaAction.QueryResult.su, 0.7));
        results.add(Pair.of(SagaAction.QueryResult.fail, 0.1));
        return RandomUitl.selectByWeight(SagaAction.QueryResult.class.getSimpleName(), results);
    }


    public static SagaAction.QueryResult sagaLocalResult() {
        Set<Pair<RemoteSagaAction.QueryResult, Double>> results = new HashSet<>();
        results.add(Pair.of(SagaAction.QueryResult.exception, 0.1));
        results.add(Pair.of(SagaAction.QueryResult.su, 0.8));
        results.add(Pair.of(SagaAction.QueryResult.fail, 0.1));
        return RandomUitl.selectByWeight(SagaAction.QueryResult.class.getSimpleName(), results);
    }


}
