package io.ddbm.pc;

import java.io.Serializable;

public interface FlowRequest {
    Serializable getId();

    String getNode();

    void setNode(String name);
}