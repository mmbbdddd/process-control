package io.ddbm.pc;

import com.google.common.base.Splitter;
import io.ddbm.pc.actions.MultipleActioin;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.SpringUtils;

import java.util.List;
import java.util.stream.Collectors;


public interface Action {

    String name();

    void run(FlowContext ctx) throws InterruptException, PauseException, ParameterException, NoSuchEventException,
                                     NoSuchNodeException;

    static Action dsl(String actionDSL) {
        if (actionDSL.contains(",")) {
            List<Action> actions = Splitter.on(",").splitToList(actionDSL).stream().map(
                n -> SpringUtils.getBean(n, Action.class)).collect(Collectors.toList());
            return new MultipleActioin(actionDSL, actions);
        } else {
            return SpringUtils.getBean(actionDSL, Action.class);
        }
    }
 
}
