package cn.hz.ddbm.pc.core;

import cn.hz.ddbm.pc.core.coast.Coasts;
import lombok.Data;

import java.util.Objects;

@Data
public class Event {
    Type   type;
    String code;

    public Event(Type type, String code) {
        this.type = type;
        this.code = code;
    }

    public static Event of(String event) {
        Type type;
        if (event.equalsIgnoreCase(Coasts.EVENT_CANCEL) || event.equalsIgnoreCase(Coasts.EVENT_PAUSE)) {
            type = Type.FLOW_EVENT;
        } else {
            type = Type.NODE_EVENT;
        }
        return new Event(type, event);
    }

    public static Event of(Type type, String event) {
        return new Event(type, event);
    }

    public static Event instantOf(String status,String to) {
        return new Event(Type.INSTANT_EVENT,String.format("toEvent(%s,%s)",status,to));
    }

    public enum Type {
        //来自外部输入的指令
        FLOW_EVENT,
        NODE_EVENT,
        EVENT_2_INSTANT,
        INSTANT_EVENT
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return type == event.type && Objects.equals(code, event.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, code);
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                ", code='" + code + '\'' +
                '}';
    }
}
