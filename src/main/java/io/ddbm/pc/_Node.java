package io.ddbm.pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 节点，分为三种
 * 1，开始节点
 * 2，结束节点
 * 3，任务节点
 * 每个节点可以响应指令，路由到目标节点
 */
public abstract class _Node implements ValueObject {
    Logger               logger = LoggerFactory.getLogger(getClass());
    String               name;
    //cmd:transation
    Map<String, Command> cmds;
    Flow                 flow;

    public _Node(String name) {
        Assert.notNull(name);
        this.name = name;
        this.cmds = new HashMap<>();
    }

    /**
     * 节点对指令的相应
     * 1，抛出异常，指令错误等
     * 2，目标节点
     * 3，异常节点
     *
     * @param cmd
     * @param ctx
     * @return
     */
    public _Node execute(String cmd, _Node currentNode, FlowContext ctx) throws RouterException {
        Assert.notNull(cmds.get(cmd), "流程节点" + ctx.getFlow().name + "." + name + "不支持指令" + cmd);
        Command command = getCmd(ctx.cmd);
        ctx.refresh(currentNode, command);
        return command.execute(ctx);
    }

    private Command getCmd(String cmd) {
        return cmds.get(cmd);
    }


    /**
     * //    节点容错策略
     * //    开始节点(action)直接返回错误
     * //    结束节点（没有action)，不会出错，如果出错，就打印
     * //    任务节点
     *
     * @param ctx
     * @param e
     */
    public abstract _Node onFail(FlowContext ctx, Exception e) throws RouterException;

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _Node node = (_Node) o;
        return Objects.equals(name, node.name) && Objects.equals(flow, node.flow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, flow);
    }
}

class Command implements ValueObject {
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


    public _Node execute(FlowContext ctx) throws RouterException {
        _Node targetNode = null;
        try {
            ctx.setCommand(this);
            action.execute(ctx);
            ctx.setException(null);
            targetNode = router.routeTo(ctx);
            return targetNode;
        } catch (Exception e) {
//            e.printStackTrace();
            ctx.setException(e);
            targetNode = ctx.getNode().onFail(ctx, e);
            return targetNode;
        } finally {
            if (null != targetNode) {
                logger.info("工作流{}记录{}状态变迁 {}=>{}", ctx.flow.name,ctx.getId(), ctx.getNode().name, targetNode.name);
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


class Start extends _Node {
    public Start(String name, Action action, Router router) {
        super(name);
        Command cmd = new Command(Flow.DEFAULT_COMMAND, action, router);
        cmd.setNode(this);
        this.cmds.put(Flow.DEFAULT_COMMAND, cmd);
    }


    @Override
    public _Node onFail(FlowContext ctx, Exception e) throws RouterException {
        throw new RuntimeException(e);
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(name);
    }
}

class End extends _Node {
    public End(String name) {
        super(name);
    }

    @Override
    public _Node onFail(FlowContext ctx, Exception e) throws RouterException {
        //TODO log
        return this;
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(name);
    }

}

class ActionNode extends _Node {

    public ActionNode(String name) {
        super(name);
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(name);
    }

    /**
     * 1，首先看是否有failNode节点定义
     * 2，其次从路由表达式中去找，
     * 3，否则以当前节点为容错节点
     */
    @Override
    public _Node onFail(FlowContext ctx, Exception e) throws RouterException {
        //执行cmd的onFail策略。
        Command cmd = ctx.getCommand();
        return cmd.onFail(ctx, e);
    }


    public void addCommand(Command command) {
        this.cmds.put(command.cmd, command);
        command.setNode(this);
    }
}
