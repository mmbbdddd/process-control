package hz.ddbm.pc.core.domain;

import hz.ddbm.pc.core.config.TransitionProperties;
import hz.ddbm.pc.core.exception.BuildFlowException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Getter
@Slf4j
public class Transition {
    final TransitionProperties attrs;
    final String               event;
    Action  action;
    Router  router;
    String  failToNode;
    Boolean saga;

    //初始化Transition的配置属性
    public Transition(TransitionProperties attrs) {
        this.event = attrs.getEvent();
        this.attrs = attrs;
    }

    //初始化Transition的bean属性（有容器、流程的上下文耦合）
    public Transition init(Node node, Flow flow) {
        this.action = buildPluginAction(node, flow);
        this.router = buildRouter(node, flow);
        this.failToNode = buildFailToNode(node, flow);
        this.saga = buildSaga(node, flow);
        return this;
    }


    private String buildFailToNode(Node node, Flow flow) {
        Assert.notNull(node, "node is null");
        if (!StringUtils.isEmpty(attrs.getFailToNode())) {
            return attrs.getFailToNode().trim();
        } else {
            return node.getName();
        }
    }

    private Boolean buildSaga(Node node, Flow flow) {
        if (!StringUtils.isEmpty(attrs.getSaga())) {
            return Boolean.valueOf(attrs.getSaga().trim());
        } else {
            return Boolean.FALSE;
        }
    }

    private Router buildRouter(Node node, Flow flow) {
        if (!StringUtils.isEmpty(attrs.getRouter())) {
            Router router = Router.dsl(attrs.getRouter().trim(), flow);
            Assert.notNull(router, "not found router for node:" + node.getName());
            return router;
        } else {
            throw new BuildFlowException("no router in transition[" + node.getName() + ":" + getEvent() + "]");
        }
    }

    private Action buildPluginAction(Node node, Flow flow) {
        if (!StringUtils.isEmpty(attrs.getAction())) {
            return Action.dsl(attrs.getAction().trim(),flow);
        } else {
            throw new BuildFlowException("no action in transition[" + node.getName() + ":" + getEvent() + "]");
        }
    }


    /**
     * 1，执行节点变迁逻辑
     * 2，如有事务，则状态线迁移道容错状态
     * 3，执行状态变迁逻辑
     *
     * @param ctx
     */
    public <T> void execute(BizContext<T> ctx) {
        Flow         flow = ctx.getFlow();
        Serializable id   = ctx.getId();
        String preNode = ctx.getNode();
        try {
            preActionPlugin(flow, ctx);
            if (this.saga) {
                ctx.setNode(this.failToNode);
                ctx.getFlow().statusService.updateNodeState(flow, id, this.failToNode, ctx);
            }
            ctx.setTransition(this);
            this.action.execute(ctx);
            postActionPlugin(flow, ctx);
        } catch (Exception e) {
            ctx.setActionError(e);
            try {
                ctx.setNode(this.failToNode);
                ctx.getFlow().statusService.updateNodeState(flow, id, this.failToNode, ctx);
                onActionExceptionPlugin(flow, preNode, e, ctx);
            } catch (Exception e2) {
                //服务失败，异常打印
                log.error("", e);
            }
        } finally {
            onActionFinallyPlugin(flow, ctx);
        }

        try {
            String newNode = getRouter().route(ctx);
            ctx.getFlow().statusService.updateNodeState(flow, id, newNode, ctx);
            ctx.setNode(newNode);
            postRoutePlugin(flow, preNode, ctx);
        } catch (Exception e) {
            log.error("", e);
            onRouterExceptionPlugin(flow, e, ctx);
            //服务失败，异常打印&暂停
            ctx.setNode(this.failToNode);
            try {
                ctx.getFlow().statusService.updateNodeState(flow, id, this.failToNode, ctx);
                ctx.getFlow().statusService.updateFlowStatus(flow, id, Flow.STAUS.PAUSE, ctx);
                log.error("", e);
            } catch (Exception e2) {
                //服务失败，异常打印
                log.error("", e2);
            }
        }
        ctx.increment(ctx.getNode());

    }


    private void preActionPlugin(Flow flow, BizContext ctx) {
        flow.getPlugins().forEach((plugin) -> {
            try {
                plugin.preAction(action.beanName(), ctx);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void postActionPlugin(Flow flow, BizContext ctx) {
        flow.getPlugins().forEach((plugin) -> {
            try {
                plugin.postAction(action.beanName(), ctx);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void onActionExceptionPlugin(Flow flow, String preNode, Exception e, BizContext ctx) {
        flow.getPlugins().forEach((plugin) -> {
            try {
                plugin.onActionException(action.beanName(), preNode, e, ctx);
            } catch (Exception e2) {
                log.error("", e2);
            }
        });
    }

    private void onActionFinallyPlugin(Flow flow, BizContext ctx) {
        flow.getPlugins().forEach((plugin) -> {
            try {
                plugin.onActionFinally(action.beanName(), ctx);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void postRoutePlugin(Flow flow, String preNode, BizContext ctx) {
        flow.getPlugins().forEach((plugin) -> {
            try {
                plugin.postRoute(action.beanName(), preNode, ctx);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void onRouterExceptionPlugin(Flow flow, Exception e, BizContext ctx) {
        flow.getPlugins().forEach((plugin) -> {
            try {
                plugin.onRouteExcetion(action.beanName(), e, ctx);
            } catch (Exception e2) {
                log.error("", e2);
            }
        });
    }


}
