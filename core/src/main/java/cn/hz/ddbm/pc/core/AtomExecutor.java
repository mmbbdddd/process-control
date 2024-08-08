package cn.hz.ddbm.pc.core;


import cn.hz.ddbm.pc.core.exception.NoRouterResultException;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
    String       event;
    List<Plugin> plugins;
    Action       action;
    Router       router;

    public void execute(FlowContext<?> ctx) throws Exception {
        Flow         flow = ctx.getFlow();
        Serializable id   = ctx.getId();
        String lastNode = ctx.getStatus()
                .getNode();
        try {
            preActionPlugin(flow, ctx);
            this.action.execute(ctx);
            postActionPlugin(flow, ctx);
        } catch (Exception e) {
            try {
                ctx.setStatus(FlowStatus.of(router.failover(lastNode, ctx)));
                onActionExceptionPlugin(flow, lastNode, e, ctx);
            } catch (Exception e2) {
                //服务失败，异常打印
                Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
            }
            throw e;
        } finally {
            onActionFinallyPlugin(flow, ctx);
            ctx.metricsNode(ctx);
        }

        try {
            String nextNode = router.route(ctx);
            ctx.setStatus(FlowStatus.of(nextNode));
            postRoutePlugin(flow, lastNode, ctx);
        } catch (Exception e) {
            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
            onRouterExceptionPlugin(flow, e, ctx);
            //服务失败，异常打印&暂停
            ctx.setStatus(FlowStatus.pause(router.failover(lastNode, ctx)));
            throw e;
        }

    }


    private void preActionPlugin(Flow flow, FlowContext<?> ctx) {

        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.preAction(action.beanName(), ctx);
                        } catch (Exception e) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);;
                        }
                    });
        });
    }

    private void postActionPlugin(Flow flow, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.postAction(action.beanName(), ctx);
                        } catch (Exception e) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);;
                        }
                    });
        });
    }

    private void onActionExceptionPlugin(Flow flow, String preNode, Exception e, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.onActionException(action.beanName(), preNode, e, ctx);
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
                            plugin.onActionFinally(action.beanName(), ctx);
                        } catch (Exception e) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);;
                        }
                    });
        });
    }

    private void postRoutePlugin(Flow flow, String preNode, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            InfraUtils.getPluginExecutorService()
                    .submit(() -> {
                        try {
                            plugin.postRoute(action.beanName(), preNode, ctx);
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
                            plugin.onRouteExcetion(action.beanName(), e, ctx);
                        } catch (Exception e2) {
                            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e2);
                        }
                    });
        });
    }

}
