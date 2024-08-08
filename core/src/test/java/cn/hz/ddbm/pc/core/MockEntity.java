package cn.hz.ddbm.pc.core;

import java.io.Serializable;

public class MockEntity implements FlowEntity {
    String id;
    String nodeStatus;
    String flowStatus;

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public FlowStatus getStatus() {
        return FlowStatus.of(flowStatus, nodeStatus);
    }
}
