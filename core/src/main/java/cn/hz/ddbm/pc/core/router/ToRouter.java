package cn.hz.ddbm.pc.core.router;


import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.Router;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 23:52
 * @Version 1.0.0
 **/


public class ToRouter implements Router {
    String from;
    String to;
    String routerName;

    public ToRouter(String from, String event, String to) {
        this.to         = to;
        this.routerName = String.format("%s>%s>%s", from, event, to);
    }

    @Override
    public String name() {
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
}
