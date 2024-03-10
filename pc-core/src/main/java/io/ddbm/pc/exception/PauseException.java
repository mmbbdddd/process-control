package io.ddbm.pc.exception;

import lombok.Getter;


/**
 * 流程暂停异常。
 * 1，连续模式被中断，通过调度中心可以再发起
 */
@Getter
public class PauseException extends FlowException {

    private final String message;

    private String flow;

    private String node;

    private Throwable cause;

    public PauseException(String flow, String node, String message, Throwable e) {
        this.flow = flow;
        this.node = node;
        this.cause = e;
        this.message = message;
    }

    public PauseException(String flow, String node, String message) {
        this.flow = flow;
        this.node = node;
        this.message = message;
    }

}
