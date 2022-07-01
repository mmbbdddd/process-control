package io.ddbm.pc.exception;

public class InterruptedException extends Exception {
    public InterruptedException(Exception e) {
        super(e);
    }

    public static InterruptedException noSuchEvent(String event, String node) {
        return null;
    }

    public static InterruptedException noSuchNode(String node) {
        return null;
    }
}
