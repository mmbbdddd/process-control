package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;

public interface ResultNotify {
    void onResult(FlowContext ctx);

    void onPauseException(PauseException e);

    void onInterruptException(InterruptException e);
}
