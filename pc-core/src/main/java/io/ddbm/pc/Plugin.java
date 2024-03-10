package io.ddbm.pc;

import io.ddbm.pc.exception.ContextCreateException;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.SessionException;
import io.ddbm.pc.status.StatusException;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;


public interface Plugin {
    void preAction(Action action, FlowContext ctx);

    void postAction(Action action, FlowContext ctx);

    void onPauseException(Action action, PauseException e, FlowContext ctx);

    void onInterruptException(Action action, InterruptException e, FlowContext ctx);

    void onArgumentException(Action action, ParameterException e, FlowContext ctx);

    void onRuntimeException(Action action, RuntimeException e, FlowContext ctx);

    void onThrowable(Action action, Throwable e, FlowContext ctx);

    void onStatusTransition(
        Action action, Pair<FlowStatus, String> sourceStatus, Pair<FlowStatus, String> targetStatus, FlowContext ctx);

    void onStateUpdate(FlowContext ctx);

    void onPluginFlowException(Throwable t, FlowContext ctx);

    void onPluginIoException(Throwable t, FlowRequest<?> request);

    void onOtherIoException(Exception e, FlowRequest<?> request);

    void onStatusException(StatusException e, FlowRequest<?> request);

    void onSessionException(SessionException e, FlowRequest<?> request);

    void onContextCreateException(ContextCreateException e, FlowRequest<?> request);

    void onNoSuchNodeException(NoSuchNodeException e, FlowContext ctx);

    void onNoSuchEventException(NoSuchEventException e, FlowContext ctx);
}
