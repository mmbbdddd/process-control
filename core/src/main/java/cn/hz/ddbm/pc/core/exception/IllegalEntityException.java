package cn.hz.ddbm.pc.core.exception;


/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 23:44
 * @Version 1.0.0
 **/


public class IllegalEntityException extends PauseFlowException {
    public IllegalEntityException(Throwable raw) {
        super(raw);
    }
}
