package io.ddbm.pc.exception;

public abstract class FlowException extends Exception {

    public FlowException() {
    }

    public FlowException(String message) {
        super(message);
    }

    public String getType() {
        return getClass().getSimpleName();
    }
}
