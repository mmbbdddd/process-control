package io.ddbm.pc.chaos;

import io.ddbm.pc.Event;
import io.ddbm.pc.Flow;
import io.ddbm.pc.Node;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class EventRule {
    String flow;

    String node;

    String event;

    //    权重
    Long weight;

    public static List<EventRule> ofNode(Flow flow, Node node) {
        return node.getEvents().values().stream().map(e -> of(flow, e)).collect(Collectors.toList());
    }

    private static EventRule of(Flow flow, Event e) {
        EventRule eventRule = new EventRule();
        eventRule.flow = flow.getName();
        eventRule.node = e.getNode().getName();
        eventRule.event = e.getEvent();
        eventRule.weight = 1l;
        return eventRule;
    }
}
