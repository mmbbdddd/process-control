package io.ddbm.pc.status.impl;

import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.status.SessionManager;
import io.ddbm.pc.status.StatusException;
import io.ddbm.pc.status.StatusManager;
import io.ddbm.pc.utils.Pair;

import java.io.Serializable;
import java.util.Map;


public class RedisManager implements StatusManager, SessionManager {
    @Override
    public Map<String, Object> getSession(String flowName, Serializable entityId) {
        return null;
    }

    @Override
    public void setSession(String flowName, Serializable entityId, Map<String, Object> session) {

    }

    @Override
    public Pair<FlowStatus, String> getStatus(String flowname, Serializable entityId)
        throws StatusException {
        return null;
    }

    @Override
    public void updateStatus(String flowname, Serializable entityId, Pair<FlowStatus, String> nodeStatus) {

    }

    @Override
    public void pause(String flowname, Serializable dataId) {

    }

    @Override
    public void recover(String flowname, Serializable dataId) {

    }

    @Override
    public void cancel(String flowname, Serializable dataId) {

    }
}
