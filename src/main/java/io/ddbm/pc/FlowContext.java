package io.ddbm.pc;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;

public class FlowContext {
    //    当前工作流数据
    Serializable        id;
    FLowRecord          record;
    //    指令
    String              cmd;
    //    入参
    Map<String, Object> args;
    Flow                flow;
    _Node               node;
    _Node               lastNode;
    //    当前变迁
    Command             command;
    Command             lastCommand;
    //    变迁异常
    Exception           exception;

    public static FlowContext of(Serializable id, FLowRecord record, String cmd, Map<String, Object> args, Flow flow, _Node node) {
        Assert.notNull(id, "上下文ID为空");
        Assert.notNull(record, "上下文Record为空");
        Assert.notNull(cmd, "上下文Comand为空");
        Assert.notNull(node, "上下文Node为空");
        FlowContext ctx = new FlowContext();
        ctx.id     = id;
        ctx.record = record;
        ctx.cmd    = cmd;
        ctx.args   = args;
        ctx.flow   = flow;
        ctx.node   = node;
        return ctx;
    }

    public _Node getNode() {
        return node;
    }

    public FLowRecord getRecord() {
        return record;
    }


    public String getCmd() {
        return cmd;
    }


    public Map<String, Object> getArgs() {
        return args;
    }


    public Serializable getId() {
        return id;
    }


    public Flow getFlow() {
        return flow;
    }


    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }


    public void refresh(_Node currentNode, Command command) {
        this.lastNode = node;
        this.node     = currentNode;
        this.command  = command;
    }

    public boolean isRetry() {
        return node.equals(lastNode) && command.equals(lastCommand);
    }
}
