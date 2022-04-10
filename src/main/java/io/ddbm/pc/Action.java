package io.ddbm.pc;

import java.util.HashMap;

public interface Action {
    Object defaultActionResult = new HashMap<>();

    static Action empty() {
        return new Action() {
            @Override
            public Object execute(FlowContext ctx) throws Exception {
                return Action.defaultActionResult;
            }
        };
    }

    Object execute(FlowContext ctx) throws Exception;
}
