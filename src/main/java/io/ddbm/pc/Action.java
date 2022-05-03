package io.ddbm.pc;

import java.util.HashMap;

public interface Action {
    Object defaultActionResult = new HashMap<>();

    static ActionWrapper empty() {
        return new ActionWrapper();
    }

    Object execute(FlowContext ctx) throws Exception;
}
