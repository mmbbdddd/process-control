package io.ddbm.pc.support;

import io.ddbm.pc.Action;
import io.ddbm.pc.FlowContext;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;
import lombok.Getter;

import java.util.List;

public class ActionPlugins {
    @Getter
    private String       actionName;
    @Getter
    private List<Action> actions;


    public ActionPlugins(String actionDsl, List<Action> actions) {
        this.actionName = actionDsl;
        this.actions    = actions;
    }


    public void execute(FlowContext ctx) throws PauseException, InterruptException {
        preAction(ctx, this);
        try {
            if (null != actions) {
                for (Action action : actions) {
                    action.execute(ctx);
                }
            }
            postAction(ctx, this);
        } catch (PauseException | InterruptException e) {
            onActionError(ctx, this, e);
            throw e;
        } catch (Exception e) {
            onActionError(ctx, this, e);
            throw new PauseException(ctx, e);
        } finally {
            actionFinally(ctx, this);
        }
    }

    private void postAction(FlowContext ctx, ActionPlugins actionWrapper) {

        ctx.getFlow().getPlugins().forEach(plugin -> {
            try {
                plugin.postAction(ctx);
            } catch (Exception e) {
                //todo
            }
        });
    }

    private void preAction(FlowContext ctx, ActionPlugins actionWrapper) {
        ctx.getFlow().getPlugins().forEach(plugin -> {
            try {
                plugin.preAction(ctx);
            } catch (Exception e) {
                //todo
            }
        });
    }

    private void onActionError(FlowContext ctx, ActionPlugins action, Exception e) {
        ctx.getFlow().getPlugins().forEach(plugin -> {
            try {
                plugin.onActionError(ctx, e);
            } catch (Exception e1) {
                //todo
            }
        });
    }

    private void actionFinally(FlowContext ctx, ActionPlugins action) {
        ctx.getFlow().getPlugins().forEach(plugin -> {
            try {
                plugin.actionFinally(ctx);
            } catch (Exception e) {
                //todo
            }
        });
    }


}
