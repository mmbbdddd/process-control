package cn.hz.ddbm.pc.core.exception;

public class RouterException extends InterruptedFlowException{
    public RouterException(Exception e) {
        super(e);
    }
}
