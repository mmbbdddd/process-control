package io.ddbm.pc.status;

import io.ddbm.pc.Flow;
import io.ddbm.pc.exception.SessionException;
import io.ddbm.pc.status.impl.InJvmManager;

import java.io.Serializable;
import java.util.Map;


public interface SessionManager {
    static SessionManager get(Flow flow) {
        return new InJvmManager();
    }

    static SessionManager getForChaos(Flow flow) {
        return new InJvmManager();
    }

    Map<String, Object> getSession(String flowName, Serializable entityId)
        throws SessionException;

    void setSession(String flowName, Serializable entityId, Map<String, Object> session)
        throws SessionException;
}
