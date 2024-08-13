package cn.hz.ddbm.pc.core.router;


import cn.hz.ddbm.pc.core.Event;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.Router;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 23:52
 * @Version 1.0.0
 **/


public class ToRouter implements Router  {
    String from;
    String to;
    String routerName;

    public ToRouter(String from, Event event, String to) {
        this.to         = to;
        this.routerName = String.format("%s||%s||%sRouter", from, event.getCode(), to);
    }

    @Override
    public String routerName() {
        return routerName;
    }

    @Override
    public String route(FlowContext<?> ctx) {
        return to;
    }

    @Override
    public String failover(String preNode, FlowContext<?> ctx) {
        return from;
    }

    @Override
    public Set<String> toNodes() {
        return new HashSet<String>() {{
            add(to);
        }};
    }

    @Override
    public String toString() {
        return "{" +
                "from:'" + from + '\'' +
                ", to:'" + to + '\'' +
                '}';
    }
}
