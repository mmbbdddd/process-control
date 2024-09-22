package cn.hz.ddbm.pc.newcore.exception;

public class LimtedRetryException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
