package hz.ddbm.pc.core.domain;

import hz.ddbm.pc.core.config.NodeProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Getter
@Slf4j
public class Node {
    final NodeProperties          attrs;
    final Type                    type;
    final String                  name;
    final Map<String, Transition> events;

    //初始化Node的配置属性
    public Node(NodeProperties attrs, List<Transition> events) {
        Assert.notNull(attrs, "attrs is null");
        Assert.notNull(attrs.getName(), "name is null");
        Assert.notNull(events, "events is null");
        this.attrs = attrs;
        this.type = attrs.getType();
        this.name = attrs.getName();
        this.events = events.stream().collect(Collectors.toMap(Transition::getEvent, Function.identity()));
    }


    //初始化Node的bean属性
    public Node init(Flow flow) {
        this.events.forEach((event, transition) -> {
            transition.init(this, flow);
        });
        return this;
    }

    /**
     * 1，执行节点变迁逻辑
     *
     * @param event
     * @param ctx
     */
    public <T> void onEvent(String event, BizContext<T> ctx) {
        Assert.isTrue(events.containsKey(event), "Node[" + name + "].events not contains event:[" + event + "]");
        Transition transition = events.get(event);
        transition.execute(ctx);
    }

    public enum Type {
        START, TASK, END
    }



}
