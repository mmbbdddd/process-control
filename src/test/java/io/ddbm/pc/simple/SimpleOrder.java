package io.ddbm.pc.simple;

import io.ddbm.pc.FlowRequest;
import io.ddbm.pc.Session;
import io.ddbm.pc.session.RequestSession;

import java.io.Serializable;

public class SimpleOrder implements FlowRequest {
    RequestSession session = new RequestSession();
    String         id      = "1";
    String         status;

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String node) {
        this.status = node;
    }

    @Override
    public Session getSession() {
        return session;
    }
}
