package io.ddbm.pc.exception;

import io.ddbm.pc.FlowRequest;
import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.utils.Pair;


public class ContextCreateException extends FlowException {

    public ContextCreateException(FlowRequest<?> request, Pair<FlowStatus, String> status, Exception e) {

    }
}
