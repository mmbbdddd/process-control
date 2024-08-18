package cn.hz.ddbm.pc.core;


import cn.hz.ddbm.pc.core.exception.wrap.ActionException;
import cn.hz.ddbm.pc.core.exception.wrap.RouterException;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 工作流/状态机的原子任务执行器。
 * @Author wanglin
 * @Date 2024/8/7 22:59
 * @Version 1.0.0
 **/

@Getter
public abstract class ActionBase<S extends Enum<S>> implements Action<S> {
    final Fsm.FsmRecordType type;
    final S                 from;
    final Event             event;
    final List<Plugin>      plugins;
    final String            actionDsl;
    final S                 failover;
    final S                 to;
    SimpleAction<S> action;

    public ActionBase(Fsm.FsmRecordType type, S from, Event event, String actionDsl, S failover, S to, List<Plugin> plugins) {
        this.event     = event;
        this.from      = from;
        this.actionDsl = actionDsl;
        this.plugins   = plugins;
        this.to        = to;
        this.type      = type;
        this.failover  = failover;
    }

    protected abstract S failover();


    public SimpleAction<S> action(FlowContext<S, ?> ctx) {
        if (null == this.action) {
            synchronized (this) {
                this.action = initAction(ctx);
            }
        }
        return this.action;
    }

    protected SimpleAction<S> initAction(FlowContext<S, ?> ctx) {
        return SimpleAction.of(actionDsl, ctx);
    }

    public void execute(FlowContext<S, ?> ctx) throws ActionException, RouterException {
        Fsm<S>       flow     = ctx.getFlow();
        Serializable id       = ctx.getId();
        S            lastNode = ctx.getStatus().getNode();
        try {
            preActionPlugin(flow, ctx);
            action(ctx).execute(ctx);
            S nextNode = nextNode(lastNode, ctx);
            ctx.setStatus(FlowStatus.of(nextNode == null ? failover() : nextNode));
            postActionPlugin(flow, lastNode, ctx);
        } catch (RouterException e) {
            ctx.setStatus(FlowStatus.of(failover()));
            onActionExceptionPlugin(flow, lastNode, e, ctx);
            throw e;
        } catch (ActionException e) {
            ctx.setStatus(FlowStatus.of(failover()));
            onActionExceptionPlugin(flow, lastNode, e, ctx);
            throw e;
        } catch (Exception e) {
            ctx.setStatus(FlowStatus.of(failover()));
            onActionExceptionPlugin(flow, lastNode, e, ctx);
            throw new ActionException(e);
        } finally {
            onActionFinallyPlugin(flow, ctx);
            ctx.metricsNode(ctx);
        }

    }

    private S nextNode(S lastNode, FlowContext<S, ?> ctx) throws RouterException {
        try {
            S nextNode = route(ctx);
            postRoutePlugin(ctx.getFlow(), lastNode, ctx);
            return nextNode;
        } catch (Exception e) {
            ctx.setStatus(FlowStatus.of(failover));
            onRouterExceptionPlugin(ctx.getFlow(), e, ctx);
            throw new RouterException(e);
        }
    }

    protected abstract S route(FlowContext<S, ?> ctx);


    private void onRouterExceptionPlugin(Fsm<S> flow, Exception e, FlowContext<S, ?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService().submit(() -> {
                try {
                    plugin.onRouteExcetion(this.action(ctx).beanName(), e, ctx);
                } catch (Exception e2) {
                    Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e2);
                }
            });
        });
    }

    private void postRoutePlugin(Fsm<S> flow, S lastNode, FlowContext<S, ?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService().submit(() -> {
                try {
                    plugin.postRoute(this.action(ctx).beanName(), lastNode, ctx);
                } catch (Exception e) {
                    Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
                }
            });
        });
    }


    private void preActionPlugin(Fsm<S> flow, FlowContext<S, ?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService().submit(() -> {
                try {
                    plugin.preAction(this.action(ctx).beanName(), ctx);
                } catch (Exception e) {
                    Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
                }
            });
        });
    }

    private void postActionPlugin(Fsm<S> flow, S lastNode, FlowContext<S, ?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService().submit(() -> {
                try {
                    plugin.postAction(this.action(ctx).beanName(), lastNode, ctx);
                } catch (Exception e) {
                    Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
                }
            });
        });
    }

    private void onActionExceptionPlugin(Fsm<S> flow, S preNode, Exception e, FlowContext<S, ?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService().submit(() -> {
                try {
                    plugin.onActionException(this.action(ctx).beanName(), preNode, e, ctx);
                } catch (Exception e2) {
                    Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e2);
                }
            });
        });
    }

    private void onActionFinallyPlugin(Fsm<S> flow, FlowContext<S, ?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService().submit(() -> {
                try {
                    plugin.onActionFinally(this.action(ctx).beanName(), ctx);
                } catch (Exception e) {
                    Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
                }
            });
        });
    }


}
