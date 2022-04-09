package io.ddbm.pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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
    public _Node execute(Flow flow, _Node node, FlowContext ctx, String cmd) throws RouterException {
        Assert.notNull(cmds.get(cmd), "流程节点" + flow.name + "." + name + "不支持指令" + cmd);
        Command command = getCmd(cmd);
        ctx.preExecute(flow, node, command);
        _Node targetNode = command.execute(ctx);
        return targetNode;

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









