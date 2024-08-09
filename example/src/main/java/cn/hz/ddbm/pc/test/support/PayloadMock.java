package cn.hz.ddbm.pc.test.support;

import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.FlowStatus;

import java.io.Serializable;

public class PayloadMock implements FlowPayload {
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
