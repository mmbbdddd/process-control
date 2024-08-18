package cn.hz.ddbm.pc.core.exception.wrap;

import cn.hz.ddbm.pc.core.exception.WrapedException;

public class RouterException extends WrapedException {

    public RouterException(Exception e) {
        super(e);
    }
}
