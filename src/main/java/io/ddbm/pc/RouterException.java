package io.ddbm.pc;

import java.io.Serializable;

public class RouterException extends Exception {
    Serializable id;
    String       flow;
    String       node;
    String       cmd;

    public RouterException(String message, FlowContext ctx, Exception e) {
        super(message + " for :" + ctx.id + "," + ctx.flow.name + "," + ctx.node.name + "," + ctx.cmd, e);
        this.id   = ctx.id;
        this.flow = ctx.flow.name;
        this.node = ctx.node.name;
        this.cmd  = ctx.cmd;
    }

    public RouterException(String message, FlowContext ctx) {
        super(message + " for :" + ctx.id + "," + ctx.flow.name + "," + ctx.node.name + "," + ctx.cmd);
        this.id   = ctx.id;
        this.flow = ctx.flow.name;
        this.node = ctx.node.name;
        this.cmd  = ctx.cmd;
    }
}
