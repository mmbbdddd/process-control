package io.ddbm.pc.status.impl;

import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.status.SessionManager;
import io.ddbm.pc.status.StatusException;
import io.ddbm.pc.status.StatusManager;
import io.ddbm.pc.utils.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class InJvmManager implements StatusManager, SessionManager {
    Map<String, Map<String, Object>> sessionMap;

    Map<String, Pair<FlowStatus, String>> statusMap;

    public InJvmManager() {
        this.sessionMap = new HashMap<>();
        this.statusMap = new HashMap<>();
    }

    @Override
    public Map<String, Object> getSession(String flowName, Serializable entityId) {
        return sessionMap.computeIfAbsent(buildKey(flowName, entityId), key -> new HashMap<>());
    }

    @Override
    public void setSession(String flowName, Serializable entityId, Map<String, Object> session) {
        sessionMap.put(buildKey(flowName, entityId), session == null ? new HashMap<>() : session);
    }

    @Override
    public Pair<FlowStatus, String> getStatus(String flowname, Serializable dateId)
        throws StatusException {
        return statusMap.get(buildKey(flowname, dateId));
    }

    @Override
    public void updateStatus(String flowname, Serializable entityId, Pair<FlowStatus, String> node) {
        statusMap.put(buildKey(flowname, entityId), node);
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

    private String buildKey(String flowName, Serializable entityId) {
        return String.format("%s:%s", flowName, entityId);
    }

}
