package io.ddbm.pc;

public class FlowContext {
    //    当前工作流数据
    FlowRequest request;
    Flow        flow;
    _Node       node;
    _Node       targetNode;
    //    当前变迁
    Command     command;
    //    变迁异常
    Exception   exception;
    Object      actionResult;

    public static FlowContext of(FlowRequest request) {
        FlowContext ctx = new FlowContext();
        ctx.request = request;
        return ctx;
    }

    public void onException(Exception actionException) {
        this.exception = actionException;
    }

    public void actionPre(Flow flow, _Node node, Command command) {
        this.flow    = flow;
        this.node    = node;
        this.command = command;
    }

    public boolean isRetry() {
        return node.equals(targetNode);
    }

    void clearActionContext() {
        this.actionResult = null;
        this.exception    = null;
    }

    public FlowResponse asResponse() throws Exception {
        if (exception != null) {
            if (exception instanceof ActionException ae) {
                throw ae.getTarget();
            } else if (exception instanceof RouterException re) {
                throw re;
            } else {
                throw exception;
            }
        } else {
            return new FlowResponse(request.getId(), request, node.name);
        }
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setTargetNode(_Node node) {
        this.targetNode = node;
    }

    public FlowRequest getRequest() {
        return request;
    }

    public _Node getNode() {
        return node;
    }

    public _Node getTargetNode() {
        return targetNode;
    }

    public Flow getFlow() {
        return flow;
    }

    public Exception getException() {
        return exception;
    }

    public void setActionResult(Object result) {
        this.actionResult = result;
    }

    public Object getActionResult() {
        return actionResult == null ? Action.defaultActionResult : actionResult;
    }

    public void resetRequestStatus() {
        this.request.setNode(targetNode.name);
    }
}
