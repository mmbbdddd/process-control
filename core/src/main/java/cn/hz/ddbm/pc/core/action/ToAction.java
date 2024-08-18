package cn.hz.ddbm.pc.core.action;

import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.action.dsl.SimpleAction;
import lombok.Getter;
import org.assertj.core.util.Sets;

import java.util.List;
import java.util.Set;

public class ToAction<S extends Enum<S>> extends ActionBase<S> implements Action<S> {

    SimpleAction<S> action;

    public ToAction(Fsm.FsmRecord<S> f , List<Plugin> plugins) {
        super(f.getType(),f.getFrom(),f.getEvent(),f.getActionDsl(),f.getFailover(),f.getTo(),plugins);
    }

    @Override
    public String beanName() {
        return action.beanName();
    }

    @Override
    protected S failover() {
        return getTo();
    }



    @Override
    protected S route(FlowContext<S, ?> ctx) {
        return null;
    }

    @Override
    public Set<S> maybeResult() {
        return Sets.set(getTo());
    }
}
