package cn.hz.ddbm.pc.core;


import cn.hz.ddbm.pc.core.exception.ActionException;
import cn.hz.ddbm.pc.core.exception.RouterException;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Builder;
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
@Builder
public class AtomExecutor {
    Event        event;
    List<Plugin> plugins;
    ActionRouter actionRouter;

    public void execute(FlowContext<?> ctx) throws ActionException, RouterException {
        Flow         flow = ctx.getFlow();
        Serializable id   = ctx.getId();
        String lastNode = ctx.getStatus()
                .getNode();
        try {
            preActionPlugin(flow, ctx);
            this.actionRouter.action(ctx).execute(ctx);
            postActionPlugin(flow, ctx);
        } catch (Exception e) {
            ctx.setStatus(FlowStatus.of(this.actionRouter.router.failover(lastNode, ctx)));
            onActionExceptionPlugin(flow, lastNode, e, ctx);
            throw new ActionException(e);
        } finally {
            onActionFinallyPlugin(flow, ctx);
            ctx.metricsNode(ctx);
        }

        try {
            String nextNode = actionRouter.router.route(ctx);
            ctx.setStatus(FlowStatus.of(nextNode));
            postRoutePlugin(flow, lastNode, ctx);
        } catch (Exception e) {
            onRouterExceptionPlugin(flow, e, ctx);
            //服务失败，异常打印&暂停
            ctx.setStatus(FlowStatus.pause(this.actionRouter.router.failover(lastNode, ctx)));
            throw new RouterException(e);
        }

    }


    private void preActionPlugin(Flow flow, FlowContext<?> ctx) {

        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.preAction(this.actionRouter.action(ctx).beanName(), ctx);
                        } catch (Exception e) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
                        }
                    });
        });
    }

    private void postActionPlugin(Flow flow, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.postAction(this.actionRouter.action(ctx).beanName(), ctx);
                        } catch (Exception e) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
                        }
                    });
        });
    }

    private void onActionExceptionPlugin(Flow flow, String preNode, Exception e, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.onActionException(this.actionRouter.action(ctx).beanName(), preNode, e, ctx);
                        } catch (Exception e2) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e2);
                        }
                    });
        });
    }

    private void onActionFinallyPlugin(Flow flow, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.onActionFinally(this.actionRouter.action(ctx).beanName(), ctx);
                        } catch (Exception e) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
                        }
                    });
        });
    }

    private void postRoutePlugin(Flow flow, String preNode, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.postRoute(this.actionRouter.router.routerName(), preNode, ctx);
                        } catch (Exception e) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
                        }
                    });
        });
    }

    private void onRouterExceptionPlugin(Flow flow, Exception e, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.onRouteExcetion(this.actionRouter.router.routerName(), e, ctx);
                        } catch (Exception e2) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e2);
                        }
                    });
        });
    }

}
