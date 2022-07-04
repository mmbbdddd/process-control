package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.utils.SpringUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedList;

public interface Action {
    static LinkedList<Action> dsl(String actionDSL) {
        if (StringUtils.isEmpty(actionDSL)) {
            return new LinkedList<>();
        } else if (actionDSL.contains(",")) {
            LinkedList<Action> actions = new LinkedList<>();
            String[]           splits  = actionDSL.split(",");
            for (String an : splits) {
                if (!StringUtils.isEmpty(an)) {
                    actions.add(SpringUtils.getBean(an, Action.class));
                }
            }
            return actions;
        } else {
            LinkedList<Action> actions = new LinkedList<>();
            actions.add(SpringUtils.getBean(actionDSL, Action.class));
            return actions;
        }
    }

    void execute(FlowContext ctx) throws PauseException, InterruptException;
}
