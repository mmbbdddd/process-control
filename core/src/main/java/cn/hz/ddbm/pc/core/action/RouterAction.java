package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.action.dsl.SimpleAction;

import java.util.List;
import java.util.Set;

public class RouterAction<S extends Enum<S>> extends ActionBase<S> implements Action<S>{
    Action<S> action;
    Set<S>    maybeResult;

    public RouterAction(Fsm.FsmRecord<S> f , List<Plugin> plugins) {
        super(f.getType(),f.getFrom(),f.getEvent(),f.getActionDsl(),f.getFailover(),f.getTo(),plugins);
    }


    @Override
    public String beanName() {
        return action.beanName();
    }

    @Override
    protected S failover() {
        return null;
    }




    @Override
    public Set<S> maybeResult() {
        return maybeResult;
    }

    public S route(FlowContext<S, ?> ctx) {
        return ctx.getStatus().getNode();
    }
}
