package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptedException;
import io.ddbm.pc.exception.PauseException;

public interface Action {
    static Action dsl(String actionDSL) {
        return null;
    }

    void execute(FlowContext ctx) throws PauseException, InterruptedException;
}
