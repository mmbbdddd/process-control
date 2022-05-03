package io.ddbm.pc;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.LinkedList;

public class ActionWrapper implements Action {
    private LinkedList<Action> actions;
    private ApplicationContext ctx;

    public ActionWrapper() {
    }

    public ActionWrapper(ApplicationContext ctx, String action) {
        Assert.notNull(action, "action is null");
        this.actions = new LinkedList<>();
        if (action.contains(",")) {
            String[] actionNames = action.split(",");
            for (String an : actionNames) {
                if (!StringUtils.isEmpty(an)) {
                    this.actions.add(ctx.getBean(an, Action.class));
                }
            }
        } else {
            this.actions.add(ctx.getBean(action, Action.class));
        }
    }


    @Override
    public Object execute(FlowContext ctx) throws Exception {
        Object result = null;
        if (actions != null) {
            for (Action action : actions) {
                result = action.execute(ctx);
            }
        }
        return result;
    }

}
