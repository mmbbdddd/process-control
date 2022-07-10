package io.ddbm.pc;

import java.io.Serializable;

public interface FlowRequest {
    Serializable getId();

    String getStatus();

    void setStatus(String node);

    Session getSession();

}
