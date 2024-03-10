package io.ddbm.pc.status;

import io.ddbm.pc.Flow;
import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.status.impl.InJvmManager;
import io.ddbm.pc.utils.Pair;

import java.io.Serializable;


public interface StatusManager {
    static StatusManager get(Flow flow) {
        return new InJvmManager();
    }

    static StatusManager getForChaos(Flow flow) {
        return new InJvmManager();
    }

    Pair<FlowStatus, String> getStatus(String flowname, Serializable dataId)
        throws StatusException;

    void updateStatus(String flowname, Serializable dataId, Pair<FlowStatus, String> nodeStatus)
        throws StatusException;

    void pause(String flowname, Serializable dataId);

    void recover(String flowname, Serializable dataId);

    void cancel(String flowname, Serializable dataId);
}
