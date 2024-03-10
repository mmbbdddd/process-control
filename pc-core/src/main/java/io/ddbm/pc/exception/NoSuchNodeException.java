package io.ddbm.pc.exception;

import lombok.Getter;


public class NoSuchNodeException extends Exception {
    @Getter
    String flow;

    @Getter
    String node;

    public NoSuchNodeException(String flow, String node) {
        this.flow = flow;
        this.node = node;
    }
}
