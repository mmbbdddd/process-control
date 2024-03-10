package io.ddbm.pc;

import io.ddbm.pc.config.Coast;
import io.ddbm.pc.utils.Pair;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;


@Data
public abstract class FlowRequest<D> {
    @NonNull
    protected Serializable dateId;

    @NonNull
    protected final D data;

    @NonNull
    protected final String flowName;

    @NonNull
    protected final String event;

    public FlowRequest(String flowName, String event, @NonNull Serializable dateId, D data) {
        this.dateId = dateId;
        this.data = data;
        this.flowName = flowName;
        this.event = event == null ? Coast.DEFAULT_EVENT : event;
    }

    public abstract Pair<FlowStatus, String> getStatus(D data);

    public abstract D setStatus(Pair<FlowStatus, String> status);
}
