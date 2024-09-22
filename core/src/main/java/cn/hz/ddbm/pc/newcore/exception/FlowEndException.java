package cn.hz.ddbm.pc.newcore.exception;

public class FlowEndException extends Exception {
    public FlowEndException() {

    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
