package cn.hz.ddbm.pc.newcore.chaos;

import cn.hutool.core.lang.Pair;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.actions.RemoteSagaAction;
import cn.hz.ddbm.pc.newcore.utils.RandomUitl;

import java.util.HashSet;
import java.util.Set;

public class ChaosConfig {
    Boolean                      mockAction;
    Integer                      retryTimes;
    Integer                      executeCount;
    Integer                      timeout;
    Set<Pair<ChaosRule, Double>> chaosRules;

    public ChaosConfig(Boolean mockAction, Integer retryTimes, Integer executeTimes, Integer timeout, Set<Pair<ChaosRule, Double>> chaosRules) {
        this.mockAction   = mockAction;
        this.retryTimes   = retryTimes;
        this.executeCount = executeTimes;
        this.timeout      = timeout;
        this.chaosRules   = chaosRules;
    }


    public void infraChaos() throws Exception {
        ChaosRule rule = RandomUitl.selectByWeight("infraChaosRule", chaosRules);
        if (rule.isException()) {
            rule.raiseException();
        }
    }


    public static ChaosConfig badOf(Boolean mockAction, Integer retryTimes, Integer executeTimes, Integer timeout) {
        return new ChaosConfig(mockAction, retryTimes, executeTimes, timeout, new HashSet<Pair<ChaosRule, Double>>() {{
            add(Pair.of(new ChaosRule(true), 4.0));
            add(Pair.of(new ChaosRule(RuntimeException.class), 1.0));
            add(Pair.of(new ChaosRule(Exception.class), 1.0));
        }});
    }

    public static ChaosConfig goodOf(Boolean mockAction, Integer retryTimes, Integer executeTimes, Integer timeout) {
        return new ChaosConfig(mockAction, retryTimes, executeTimes, timeout, new HashSet<Pair<ChaosRule, Double>>() {{
            add(Pair.of(new ChaosRule(true), 10.0));
        }});
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
