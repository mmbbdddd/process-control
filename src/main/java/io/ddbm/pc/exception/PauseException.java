package io.ddbm.pc.exception;

public class PauseException extends Exception {
    private String    action;
    private Exception cause;

    public PauseException(Exception e) {
        super(e);
        this.cause = e;
    }

}
