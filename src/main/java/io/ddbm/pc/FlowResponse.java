package io.ddbm.pc;

import java.io.Serializable;

public class FlowResponse {
    Serializable id;
    FlowRequest  request;
    String       node;
    Exception    exception;

    public FlowResponse(Serializable id, FlowRequest request, String node) {
        this.id      = id;
        this.request = request;
        this.node    = node;
    }

    public static FlowResponse error() {
        //todo
        return null;
    }
}
