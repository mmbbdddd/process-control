package io.ddbm.pc;

import io.ddbm.pc.support.ActionPlugins;

public interface Plugin {
    void preAction(FlowContext ctx, Action action);

    void postAction(FlowContext ctx, Action action);

    void actionFinally(FlowContext ctx, ActionPlugins action);

    void onActionError(FlowContext ctx, ActionPlugins action, Exception e);
}
