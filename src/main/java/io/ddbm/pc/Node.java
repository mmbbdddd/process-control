package io.ddbm.pc;

import org.springframework.util.Assert;

public class Node extends _Node {

    public Node(String name) {
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