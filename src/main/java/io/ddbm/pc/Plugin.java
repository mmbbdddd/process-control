package io.ddbm.pc;

public interface Plugin {
    void preAction(FlowContext ctx);

    void postAction(FlowContext ctx);

    void actionFinally(FlowContext ctx);

    void onActionError(FlowContext ctx, Exception e);
}
