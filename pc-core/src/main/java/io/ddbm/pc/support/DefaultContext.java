package io.ddbm.pc.support;

import io.ddbm.pc.Flow;
import io.ddbm.pc.FlowRequest;
import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.RunMode;
import io.ddbm.pc.exception.ContextCreateException;
import io.ddbm.pc.utils.Pair;

import java.util.Map;


public class DefaultContext extends FlowContext {
    public DefaultContext(
        RunMode runMode, FlowRequest<?> request, Flow flow, Pair<FlowStatus, String> status,
        Map<String, Object> session)
        throws ContextCreateException {
        super(runMode, request, flow, status, session);
    }
} 
 