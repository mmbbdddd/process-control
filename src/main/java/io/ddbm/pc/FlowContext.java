package io.ddbm.pc;

public class FlowContext {
    //    当前工作流数据
    FlowRequest request;
    Flow        flow;
    _Node       node;
    _Node       lastNode;
    //    当前变迁
    Command     command;
    //    变迁异常
    Exception   actionException;
    Object      actionResult;

    public static FlowContext of(FlowRequest request) {
        FlowContext ctx = new FlowContext();
        ctx.request = request;
        return ctx;
    }


    public _Node getNode() {
        return node;
    }

    public FlowRequest getRequest() {
        return request;
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

    public Exception getActionException() {
        return actionException;
    }

    public void setActionException(Exception actionException) {
        this.actionException = actionException;
    }


    public void preExecute(Flow flow, _Node node, Command command) {
        this.flow     = flow;
        this.node     = node;
        this.command  = command;
    }

    public void postExecute(_Node node) {
        this.lastNode = this.node;
        this.node     = node;
        this.command  = null;
        this.request.setNode(node.name);
    }

    public boolean isRetry() {
        return node.equals(lastNode);
    }

    void clearActionContext() {
        this.actionResult    = null;
        this.actionException = null;
    }

    public Object getActionResult() {
        return actionResult;
    }

    public void setActionResult(Object actionResult) {
        this.actionResult = actionResult;
    }


}
