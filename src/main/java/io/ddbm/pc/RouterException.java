package io.ddbm.pc;

import java.io.Serializable;

public class RouterException extends Exception {
    Serializable id;
    String       flow;
    String       node;
    String       cmd;

    public RouterException(String message) {
        super(message);

    }

    public RouterException(String message, FlowContext ctx, Exception e) {
        super(message + " for :" + ctx.getRequest().getId() + "," + ctx.flow.name + "," + ctx.node.name + "," + ctx.command.cmd, e);
        this.id   = ctx.getRequest().getId();
        this.flow = ctx.flow.name;
        this.node = ctx.node.name;
        this.cmd  = ctx.command.cmd;
    }

    public RouterException(String message, FlowContext ctx) {
        super(message + " for :" + ctx.getRequest().getId() + "," + ctx.flow.name + "," + ctx.node.name + "," + ctx.command.cmd);
        this.id   = ctx.getRequest().getId();
        this.flow = ctx.flow.name;
        this.node = ctx.node.name;
        this.cmd  = ctx.command.cmd;
    }
}
