package cn.hz.ddbm.pc.core.router;


import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.Event;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.Router;
import lombok.Getter;

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
    @Getter
    String to;
    String routerName;

    public ToRouter(String from,  String to) {
        Assert.notNull(from,"from is null");
        Assert.notNull(to,"to is null");
        this.to         = to;
        this.from = from;
        this.routerName = String.format("fromToRouter(%s,%s)", from,  to);
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
