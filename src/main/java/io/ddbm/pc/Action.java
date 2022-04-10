package io.ddbm.pc;

public interface Action {
    Object execute(FlowContext ctx) throws Exception;
}
