package cn.hz.ddbm.pc.core;


import cn.hz.ddbm.pc.core.coast.Coasts;
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
    final Fsm.FsmRecord<S> fr;
    final List<Plugin>     plugins;

    SimpleAction<S> action;

    public ActionBase(Fsm.FsmRecord<S> fr, List<Plugin> plugins) {
        this.fr      = fr;
        this.plugins = plugins;
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
        return SimpleAction.of(fr.getActionDsl(), ctx);
    }

    public void execute(FlowContext<S, ?> ctx) throws ActionException, RouterException {
        Fsm<S>       flow     = ctx.getFlow();
        Serializable id       = ctx.getId();
        String       event    = ctx.getEvent();
        State<S>     lastNode = ctx.getStatus();
        try {
            preActionPlugin(flow, ctx);
            action(ctx).execute(ctx);
            S nextNode = nextNode(lastNode.name, ctx);
            ctx.getStatus().flush(event, nextNode == null ? failover() : nextNode, flow);
            postActionPlugin(flow, lastNode.name, ctx);
        } catch (RouterException e) {
            ctx.getStatus().flush(event, failover(), flow);
            onActionExceptionPlugin(flow, lastNode.name, e, ctx);
            throw e;
        } catch (ActionException e) {
            ctx.getStatus().flush(event, failover(), flow);
            onActionExceptionPlugin(flow, lastNode.name, e, ctx);
            throw e;
        } catch (Exception e) {
            ctx.getStatus().flush(event, failover(), flow);
            onActionExceptionPlugin(flow, lastNode.name, e, ctx);
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
            ctx.getStatus().flush(Coasts.EVENT_DEFAULT, failover(), ctx.getFlow());
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
