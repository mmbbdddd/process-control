package io.ddbm.pc.exception;

import io.ddbm.pc.FlowContext;
import lombok.Getter;

/**
 * 流程暂停异常。
 */
@Getter
public class PauseException extends Exception {

    private FlowContext ctx;
    private Exception   cause;

    public PauseException(FlowContext ctx, Exception e) {
        super(e);
        this.cause = e;
        this.ctx   = ctx;
    }

    public PauseException(FlowContext ctx, String msg) {
        super(msg);
        this.ctx = ctx;
    }

    @Override
    public synchronized Throwable getCause() {
        return cause;
    }


    @Override
    public StackTraceElement[] getStackTrace() {
        return null;
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        super.setStackTrace(null);
    }
}
