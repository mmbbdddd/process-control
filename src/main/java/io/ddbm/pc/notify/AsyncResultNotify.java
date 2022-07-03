package io.ddbm.pc.notify;

import io.ddbm.pc.FlowContext;

public class AsyncResultNotify {

    private FlowContext ctx;
    private Exception   e;

    public void setException(Exception e) {
        this.e = e;
    }
    public void setResult(FlowContext ctx) {
        this.ctx = ctx;
    }

    public FlowContext getResult() throws Exception {
        if (null != e) {
            throw e;
        } else {
            return ctx;
        }
    }
}
