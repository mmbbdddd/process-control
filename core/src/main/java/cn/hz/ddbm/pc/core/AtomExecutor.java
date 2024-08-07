package cn.hz.ddbm.pc.core;


import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 22:59
 * @Version 1.0.0
 **/

@Slf4j
public class AtomExecutor {
    String       event;
    List<Plugin> plugins;
    Action       action;
    Router       router;

    public void execute(FlowContext<?> ctx) {
        Flow         flow    = ctx.getFlow();
        Serializable id      = ctx.getId();
        String       preNode = ctx.getStatus().getNode();
        try {
            preActionPlugin(flow, ctx);
            this.action.execute(ctx);
            postActionPlugin(flow, ctx);
        } catch (Exception e) {
            try {
                ctx.setStatus(FlowStatus.of(router.failover()));
                onActionExceptionPlugin(flow, preNode, e, ctx);
            } catch (Exception e2) {
                //服务失败，异常打印
                log.error("", e);
            }
        } finally {
            onActionFinallyPlugin(flow, ctx);
        }

        try {
            String newNode = router.route(ctx);
            ctx.setStatus(FlowStatus.of(newNode));
            postRoutePlugin(flow, preNode, ctx);
        } catch (Exception e) {
            log.error("", e);
            onRouterExceptionPlugin(flow, e, ctx);
            //服务失败，异常打印&暂停
            ctx.setStatus(FlowStatus.of(router.failover()));
        }
        ctx.metricsNode(ctx.getStatus().getNode());
    }


    private void preActionPlugin(Flow flow, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            try {
                plugin.preAction(action.beanName(), ctx);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void postActionPlugin(Flow flow, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            try {
                plugin.postAction(action.beanName(), ctx);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void onActionExceptionPlugin(Flow flow, String preNode, Exception e, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            try {
                plugin.onActionException(action.beanName(), preNode, e, ctx);
            } catch (Exception e2) {
                log.error("", e2);
            }
        });
    }

    private void onActionFinallyPlugin(Flow flow, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            try {
                plugin.onActionFinally(action.beanName(), ctx);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void postRoutePlugin(Flow flow, String preNode, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            try {
                plugin.postRoute(action.beanName(), preNode, ctx);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void onRouterExceptionPlugin(Flow flow, Exception e, FlowContext<?> ctx) {
        plugins.forEach((plugin) -> {
            try {
                plugin.onRouteExcetion(action.beanName(), e, ctx);
            } catch (Exception e2) {
                log.error("", e2);
            }
        });
    }

}
