package io.ddbm.pc.chaos;

import io.ddbm.pc.Event;
import io.ddbm.pc.Flow;
import io.ddbm.pc.FlowRequest;
import io.ddbm.pc.config.Coast;
import io.ddbm.pc.factory.FlowFactory;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.SpringUtils;
import io.ddbm.pc.utils.WeightRandom;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class ChaosService implements InitializingBean {

    Map<String, List<EventRule>> eventRules;

    Map<String, List<ActionResultRule>> actionResultRules;

    @Override
    public void afterPropertiesSet()
        throws Exception {
        if (SpringUtils.getBeansOfType(ChaosRuleLoader.class).size() > 0) {
            //生成流程的混沌规则
            eventRules = SpringUtils.getBean(ChaosRuleLoader.class).queryAllEventRules().stream().collect(
                Collectors.groupingBy(EventRule::getFlow));
            actionResultRules = SpringUtils.getBean(ChaosRuleLoader.class).queryAllActionResultRules().stream().collect(
                Collectors.groupingBy(ActionResultRule::getFlow));
        } else {
            this.eventRules = new HashMap<>();
            this.actionResultRules = new HashMap<>();
            //        如果规则为空，则使用默认的随机规则
            FlowFactory.allFlowNames().forEach(flowName -> {
                if (eventRules.get(flowName) == null) {
                    eventRules.put(flowName, createEventRules(FlowFactory.get(flowName)));
                }
                if (actionResultRules.get(flowName) == null) {
                    actionResultRules.put(flowName, createActionResultRules(FlowFactory.get(flowName)));
                }
            });
        }
    }

    private List<ActionResultRule> createActionResultRules(Flow flow) {
        List<ActionResultRule> rules = flow.getNodes().values().stream().map(
            node -> ActionResultRule.ofNode(flow, node)).flatMap(Collection::stream).collect(Collectors.toList());

        return rules;
    }

    private List<EventRule> createEventRules(Flow flow) {
        List<EventRule> rules = flow.getNodes().values().stream().map(node -> EventRule.ofNode(flow, node)).flatMap(
            Collection::stream).collect(Collectors.toList());
        rules.forEach(rule -> {
            if (rule.event.equals(Coast.DEFAULT_EVENT)) {
                rule.weight = Coast.DEFAULT_WEIGHT;
            }
        });

        return rules;
    }

    /**
     * 统计某请求的异常
     *
     * @param request
     * @param node
     * @param e
     */
    public void count(FlowRequest<?> request, String node, Throwable e) {
        count(request.getFlowName(), request.getDateId(), node, request.getEvent(), e.getClass().getSimpleName());
    }

    /**
     * 统计流程：节点的各种指标（label）
     *
     * @param flow
     * @param flowId
     * @param node
     * @param event
     * @param lable
     */
    public void count(String flow, Serializable flowId, String node, String event, String lable) {

    }

    /**
     * 按配置的策略产生流程支持的事件
     * <p>
     * 算法
     * 可用事件AB，
     * A规则：权重1，
     * B规则：权重2，
     * 其中，总概率为 totalWeight =  3
     * A的概率为 1/3
     * A的概率为 2/3
     *
     * @param flowName
     * @return
     */
    public String produceEvent(String flowName, String node) {
        List<EventRule> eventRulesForFLow = eventRules.get(flowName);
        ;
        if (eventRulesForFLow == null) {
            eventRulesForFLow = createEventRules(FlowFactory.get(flowName));
        }
        List<EventRule> availableRules = new ArrayList<>();
        for (EventRule eventRule : eventRulesForFLow) {
            if (Objects.equals(node, eventRule.getNode())) {
                availableRules.add(eventRule);
            }
        }
        WeightRandom<EventRule> weightRandom = new WeightRandom<>();
        availableRules.forEach(r -> weightRandom.add(r, r.getWeight()));

        EventRule event = weightRandom.next();
        if (Objects.isNull(event) || Objects.isNull(event.getEvent())) {
            return Coast.DEFAULT_EVENT;
        } else {
            return event.getEvent();
        }
    }

    /**
     * 按配置的策略产生Action的结果
     *
     * @param event
     * @param ctx   执行上下文
     * @return
     */
    public void mockActionResult(Event event, FlowContext ctx)
        throws Throwable {
        String action = event.getAction();
        String flow = ctx.getFlow().getName();
        List<ActionResultRule> actionResultRuleForFlow = actionResultRules.get(flow);
        ;
        if (actionResultRuleForFlow == null) {
            actionResultRuleForFlow = createActionResultRules(FlowFactory.get(flow));
        }
        List<ActionResultRule> availableRules = actionResultRuleForFlow.stream().filter(
            r -> Objects.equals(action, r.getAction())).collect(Collectors.toList());
        WeightRandom<ActionResultRule> weightRandom = new WeightRandom<>();
        availableRules.forEach(r -> weightRandom.add(r, r.getWeight()));
        ActionResultRule selected = weightRandom.next();

        if (Objects.isNull(selected) || Objects.isNull(selected.getException())) {
            //正常执行
        } else {
            selected.throwExcetion(ctx, event);
        }
    }

}
