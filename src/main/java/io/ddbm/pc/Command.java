package io.ddbm.pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class Command implements ValueObject {
    Logger logger = LoggerFactory.getLogger(getClass());
    String cmd;
    Action action;
    Router router;
    String failNode;
    _Node  node;


    public Command(String cmd, Action action, Router router) {
        Assert.notNull(cmd);
        Assert.notNull(action);
        Assert.notNull(router);
        this.cmd    = cmd;
        this.action = action;
        this.router = router;
    }


    public void execute(FlowContext ctx) {
        ctx.clearActionContext();
        try {
            ctx.setCommand(this);
            action.execute(ctx);
            ctx.setTargetNode(router.routeTo(ctx));
        } catch (RouterException e) {
            ctx.onException(e);
            ctx.setTargetNode(onFail(ctx, e));
        } catch (Exception e) {
            ctx.onException(new ActionException(action, e));
            ctx.setTargetNode(onFail(ctx, e));
        } finally {
            if (null != ctx.targetNode) {
                logger.info("工作流{}记录{}状态变迁 {}=>{}", ctx.flow.name, ctx.getRequest().getId(), ctx.getNode().name, ctx.targetNode.name);
            }
        }
    }

    public _Node onFail(FlowContext ctx, Exception e) {
        //如果failNode定义，丢到failNode
//        如果failNode未定义，当前节点
        return ctx.flow.getNode(failNode);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(node);
        Assert.notNull(failNode);
    }

    public void setNode(_Node node) {
        this.node = node;
        if (StringUtils.isEmpty(failNode)) {
            this.failNode = node.name;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(cmd, command.cmd) && Objects.equals(node, command.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cmd, node);
    }
}