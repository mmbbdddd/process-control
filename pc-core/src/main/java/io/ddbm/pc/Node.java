package io.ddbm.pc;

import io.ddbm.pc.exception.NoSuchEventException;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 节点，分为三种
 * 每个节点可以响应指令，路由到目标节点
 */
@Data
public class Node {
    final Flow flow;

    final Type type;

    final  String name;

    final Map<String, Event> events;

    public Node(Flow flow,Type type, String name) {
        Assert.notNull(type,"type is null");
        Assert.notNull(name,"name is null");
        Assert.notNull(flow,"flow is null");
        this.flow = flow;
        this.type = type;
        this.name = name;
        this.events = new HashMap<>();
    }

    public Event getEvent(String event)
        throws NoSuchEventException {
        if (!events.containsKey(event)) {
            throw new NoSuchEventException(name, event);
        }
        return events.get(event);
    }

    public void on(String event, String action, String to, String router, String desc, String retryCount) {
        this.onEvent(event, new Event(flow,this, event, action, to, router, desc, retryCount));
    }

    private void onEvent(String e, Event event) {
        this.events.put(e, event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Node node = (Node)o;
        return type == node.type && Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }

    public enum Type {
        START,
        END,
        NODE
    }
}









