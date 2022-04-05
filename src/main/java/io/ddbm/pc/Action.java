package io.ddbm.pc;

public interface Action {
    void execute(FlowContext ctx) throws Exception;
}
