package io.ddbm.pc.actions;

import io.ddbm.pc.Action;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.support.FlowContext;

import java.util.List;


public class MultipleActioin implements Action {
    private final String name;

    private final List<Action> actions;

    public MultipleActioin(String name, List<Action> actions) {
        this.name = name;
        this.actions = actions;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void run(FlowContext ctx) throws InterruptException, PauseException, ParameterException,
                                            NoSuchEventException, NoSuchNodeException {
        for (Action action : actions) {
            action.run(ctx);
        }
    }
 
}
