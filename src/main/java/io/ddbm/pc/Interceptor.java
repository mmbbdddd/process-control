package io.ddbm.pc;

public interface Interceptor {
    void preAction(FlowContext ctx);

    void postAction(FlowContext ctx);
}
