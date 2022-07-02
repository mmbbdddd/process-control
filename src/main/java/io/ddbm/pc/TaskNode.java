package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 节点，分为三种
 * 每个节点可以响应指令，路由到目标节点
 */
@Data
public class TaskNode {
    Type               type;
    String             name;
    Boolean            fluent;
    Map<String, Event> events;

    public TaskNode(Type type, String name, String fluent) {
        Assert.notNull(type);
        Assert.notNull(name);
        this.type   = type;
        this.name   = name;
        this.fluent = StringUtils.isEmpty(fluent) ||  fluent.endsWith("true");
        this.events = new HashMap<>();
    }


    public Event getEvent(String event) throws InterruptException {
        if (!events.containsKey(event)) {
            throw InterruptException.noSuchEvent(event, name);
        }
        return events.get(event);
    }

    public void on(String event, String actionDSL, String maybe, String desc, String retry) {
        this.onEvent(event, new Event(this, event, actionDSL, maybe, desc, retry));
    }

    private void onEvent(String e, Event event) {
        this.events.put(e, event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskNode node = (TaskNode) o;
        return type == node.type && Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }


    public enum Type {
        START, END, TASK
    }
}









