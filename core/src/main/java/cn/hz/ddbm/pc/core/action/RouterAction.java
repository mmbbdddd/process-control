package cn.hz.ddbm.pc.core.action;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.exception.wrap.ActionException;
import cn.hz.ddbm.pc.core.exception.wrap.RouterException;

import java.util.List;
import java.util.Set;

public class RouterAction<S extends Enum<S>> extends ActionBase<S> {
    Set<S> maybeResult;

    public RouterAction(Fsm.FsmRecord<S> f, Set<S> maybeResult, List<Plugin> plugins) {
        super(f.getType(), f.getFrom(), f.getEvent(), f.getActionDsl(), f.getFailover(), f.getTo(), plugins);
        this.maybeResult = maybeResult;
    }


    @Override
    public String beanName() {
        return getAction().beanName();
    }

    @Override
    protected S failover() {
        return getFrom();
    }

    public void execute(FlowContext<S, ?> ctx) throws ActionException, RouterException {
        ctx.setNextNode(null);
        super.execute(ctx);
    }


    public S route(FlowContext<S, ?> ctx) {
        Assert.notNull(ctx.getNextNode(), "routerAction 必须设置ctx.setNextNode()");
        return ctx.getNextNode();
    }

    @Override
    public Set<S> maybeResult() {
        return maybeResult;
    }
}
