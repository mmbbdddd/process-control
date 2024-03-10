package io.ddbm.pc.exception;

import lombok.Getter;


@Getter
public class MockException extends RuntimeException {
    private final String node;

    private final String flow;

    private final Throwable cause;

    public MockException(String flow, String node, Throwable e) {
        this.flow = flow;
        this.node = node;
        this.cause = e;
    }

}
