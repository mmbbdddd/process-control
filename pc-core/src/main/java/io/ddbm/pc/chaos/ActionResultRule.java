package io.ddbm.pc.chaos;

import io.ddbm.pc.Event;
import io.ddbm.pc.Flow;
import io.ddbm.pc.Node;
import io.ddbm.pc.config.Coast;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.ExceptionUtils;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Data
public class ActionResultRule {
    String flow;

    String action;

    //    权重
    Long weight;

    Class<? extends Throwable> exception;

    public void throwExcetion(FlowContext ctx, Event event)
        throws Throwable {
        Throwable e = ExceptionUtils.throwExcetion(exception, event, ctx);
        if (Objects.nonNull(e)) {
            //            e.setStackTrace(Lists.newArrayList().toArray(new StackTraceElement[0]));
            throw e;
        }
    }

    public static List<ActionResultRule> ofNode(Flow flow, Node node) {
        return node.getEvents().values().stream().map(event -> ofAction(flow, event.getAction())).flatMap(
            Collection::stream).collect(Collectors.toList());
    }

    private static List<ActionResultRule> ofAction(Flow flow, String action) {
        List<Class<? extends Throwable>> throwables = ExceptionUtils.exceptionTypes;
        return throwables.stream().map(exception -> of(flow, action, exception)).collect(Collectors.toList());
    }

    private static ActionResultRule of(
        Flow flow, String action, Class<? extends Throwable> exception) {
        ActionResultRule actionResultRule = new ActionResultRule();
        actionResultRule.flow = flow.getName();
        actionResultRule.action = action;
        actionResultRule.weight = Objects.isNull(exception) ? Coast.DEFAULT_WEIGHT : 1l;
        actionResultRule.exception = exception;
        return actionResultRule;
    }

}
