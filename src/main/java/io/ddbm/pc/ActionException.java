package io.ddbm.pc;

public class ActionException extends Exception {
    private final String    action;
    private final Exception target;

    public ActionException(Action action, Exception e) {
        this.target = e;
        this.action = action.getClass().getSimpleName();
    }

    public String getAction() {
        return action;
    }

    public Exception getTarget() {
        return target;
    }
}
