package io.ddbm.pc.exception;

import lombok.Getter;


public class NoSuchEventException extends Exception {
    @Getter
    String node;

    @Getter
    String event;

    public NoSuchEventException(String node, String event) {
        this.node = node;
        this.event = event;
    }
}
