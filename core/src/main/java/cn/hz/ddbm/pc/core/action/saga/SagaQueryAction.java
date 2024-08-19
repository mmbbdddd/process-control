package cn.hz.ddbm.pc.core.action.saga;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.coast.Coasts;

public interface SagaQueryAction<S extends Enum<S>> extends Action<S> {
    @Override
    default void execute(FlowContext<S, ?> ctx) throws Exception {
        S currentState =  queryCurrentStatus(ctx);
        ctx.setSession(Coasts.STATUS,currentState);
    }

    S queryCurrentStatus(FlowContext<S, ?> ctx);
}
