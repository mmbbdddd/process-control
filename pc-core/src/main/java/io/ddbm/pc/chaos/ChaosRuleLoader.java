package io.ddbm.pc.chaos;

import java.util.List;


public interface ChaosRuleLoader {

    List<EventRule> queryAllEventRules();

    List<ActionResultRule> queryAllActionResultRules();
}
