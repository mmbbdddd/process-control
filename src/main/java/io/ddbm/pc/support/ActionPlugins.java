package io.ddbm.pc.support;

import io.ddbm.pc.Action;
import io.ddbm.pc.FlowContext;
import io.ddbm.pc.exception.InterruptedException;
import io.ddbm.pc.exception.PauseException;
import lombok.Getter;

public class ActionPlugins {
    @Getter
    private String actionName;
    @Getter
    private Action action;


    public ActionPlugins(String actionDsl, Action action) {
        this.actionName = actionDsl;
        this.action     = action;
    }


    public void execute(FlowContext ctx) throws PauseException, InterruptedException {
        preAction(ctx, this);
        try {
            action.execute(ctx);
            postAction(ctx, this);
        } catch (PauseException | InterruptedException e) {
            onActionError(ctx, this, e);
            throw e;
        } catch (Exception e) {
            onActionError(ctx, this, e);
            throw new PauseException(e);
        } finally {
            actionFinally(ctx, this);
        }
    }

    private void postAction(FlowContext ctx, ActionPlugins actionWrapper) {

        ctx.getFlow().getPlugins().forEach(plugin -> {
            try {
                plugin.postAction(ctx, action);
            } catch (Exception e) {
                //todo
            }
        });
    }

    private void preAction(FlowContext ctx, ActionPlugins actionWrapper) {
        ctx.getFlow().getPlugins().forEach(plugin -> {
            try {
                plugin.preAction(ctx, action);
            } catch (Exception e) {
                //todo
            }
        });
    }

    private void onActionError(FlowContext ctx, ActionPlugins action, Exception e) {
        ctx.getFlow().getPlugins().forEach(plugin -> {
            try {
                plugin.onActionError(ctx, action, e);
            } catch (Exception e1) {
                //todo
            }
        });
    }

    private void actionFinally(FlowContext ctx, ActionPlugins action) {
        ctx.getFlow().getPlugins().forEach(plugin -> {
            try {
                plugin.actionFinally(ctx, action);
            } catch (Exception e) {
                //todo
            }
        });
    }


}
