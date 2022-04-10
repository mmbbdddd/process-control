package io.ddbm.pc;

public class ActionException extends Exception {
    private final String action;

    public ActionException(Action action, Exception e) {
        super(e);
        this.action = action.getClass().getSimpleName();
    }
}
