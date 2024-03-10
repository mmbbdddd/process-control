package io.ddbm.pc;

import io.ddbm.pc.utils.Pair;
import lombok.NonNull;

import java.io.Serializable;


public class TestFlowRequest extends FlowRequest<Pair<FlowStatus, String>> {
    Pair<FlowStatus, String> status;

    public TestFlowRequest(
        String flowName, String event, @NonNull Serializable dateId, Pair<FlowStatus, String> data) {
        super(flowName, event, dateId, data);
    }

    @Override
    public Pair<FlowStatus, String> getStatus(Pair<FlowStatus, String> data) {
        return status;
    }

    @Override
    public Pair<FlowStatus, String> setStatus(Pair<FlowStatus, String> status) {
        this.status = status;
        return this.status;
    }
}
