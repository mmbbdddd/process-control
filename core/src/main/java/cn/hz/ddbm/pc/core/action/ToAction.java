package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.*;
import org.assertj.core.util.Sets;

import java.util.List;
import java.util.Set;

public class ToAction<S extends Enum<S>> extends ActionBase<S>   {


    public ToAction(Fsm.FsmRecord<S> f , List<Plugin> plugins) {
        super(f.getType(),f.getFrom(),f.getEvent(),f.getActionDsl(),f.getFailover(),f.getTo(),f.getRouter(),plugins);
    }

    @Override
    public String beanName() {
        return getAction().beanName();
    }

    @Override
    protected S failover() {
        return getFrom();
    }



    @Override
    protected S route(FlowContext<S, ?> ctx) {
        return getTo();
    }
}
