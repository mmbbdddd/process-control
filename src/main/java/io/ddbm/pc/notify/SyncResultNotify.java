package io.ddbm.pc.notify;

import io.ddbm.pc.FlowContext;
import io.ddbm.pc.ResultNotify;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.TimeoutException;
import io.ddbm.pc.utils.TimeoutWatch;

public abstract class SyncResultNotify implements ResultNotify {
    protected final TimeoutWatch     timeout;
    protected     TimeoutException e;
    protected     FlowContext      result;

    public SyncResultNotify(TimeoutWatch timeout) {
        this.timeout = timeout;
    }


    @Override
    public void onInterruptException(InterruptException e) {
        throw e;
    }

    public FlowContext getResult() {
        if (null != e) {
            throw e;
        } else {
            return result;
        }
    }
}
