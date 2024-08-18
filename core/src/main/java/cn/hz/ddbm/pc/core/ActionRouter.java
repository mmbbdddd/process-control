package cn.hz.ddbm.pc.core;


import cn.hz.ddbm.pc.core.exception.ActionException;
import cn.hz.ddbm.pc.core.exception.RouterException;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.router.ToRouter;
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
public class ActionRouter<S extends Enum<S>> {
    Event        event;
    List<Plugin> plugins;
    String       actionDsl;
    S            failover;
    Router<S>    router;

    public ActionRouter(Fsm.FsmRecordType type, S from, Event event, String actionDsl, S failover, Router<S> router, List<Plugin> plugins) {
        this.event     = event;
        this.router    = router;
        this.actionDsl = actionDsl;
        this.plugins   = plugins;
        switch (type) {
            case ROUTER: {
                this.failover = from;
                break;
            }
            case SAGA: {
                this.failover = failover;
                break;
            }
            case TO: {
                this.failover = ((ToRouter<S>) router).getFrom();
                break;
            }
        }
    }


    public Action<S> action(FlowContext<S, ?> ctx) {
        return Action.of(actionDsl, ctx);
    }

    public void execute(FlowContext<S, ?> ctx) throws ActionException, RouterException {
        Fsm<S>       flow     = ctx.getFlow();
        Serializable id       = ctx.getId();
        S            lastNode = ctx.getStatus().getNode();
        try {
            preActionPlugin(flow, ctx);
            S nextNode = action(ctx).execute(ctx);
            ctx.setStatus(FlowStatus.of(nextNode));
            postActionPlugin(flow, ctx);
        } catch (Exception e) {
            ctx.setStatus(FlowStatus.of(failover));
            onActionExceptionPlugin(flow, lastNode, e, ctx);
            throw new ActionException(e);
        } finally {
            onActionFinallyPlugin(flow, ctx);
            ctx.metricsNode(ctx);
        }

//        try {
//            S nextNode = actionRouter.router.route(ctx);
//            ctx.setStatus(FlowStatus.of(nextNode));
//            postRoutePlugin(flow, lastNode, ctx);
//        } catch (Exception e) {
//            onRouterExceptionPlugin(flow, e, ctx);
//            //服务失败，异常打印&暂停
//            ctx.setStatus(FlowStatus.pause(this.actionRouter.router.failover(lastNode, ctx)));
//            throw new RouterException(e);
//        }

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

    private void postActionPlugin(Fsm<S> flow, FlowContext<S, ?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService().submit(() -> {
                try {
                    plugin.postAction(this.action(ctx).beanName(), ctx);
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
