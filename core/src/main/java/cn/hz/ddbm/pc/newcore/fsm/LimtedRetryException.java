package cn.hz.ddbm.pc.newcore.fsm;

public class LimtedRetryException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
