package io.ddbm.pc.exception;

import lombok.Getter;


/**
 * 流程停止异常。
 */
public class InterruptException extends FlowException {
    @Getter
    private String node;

    @Getter
    private Throwable cause;

    public InterruptException(String node, Throwable e) {
        this.node = node;
        this.cause = e;
    }

    public InterruptException(String node, String message) {
        super(message);
        this.node = node;
    }

}
