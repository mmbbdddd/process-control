package cn.hz.ddbm.pc.test.support;

import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.FlowStatus;
import cn.hz.ddbm.pc.core.Fsm;

import java.io.Serializable;
import java.util.UUID;

public class PayloadMock implements FlowPayload {
    String id;
    String nodeStatus;
    String flowStatus;

    public PayloadMock(String init) {
        this.id         = UUID.randomUUID().toString();
        this.flowStatus = Fsm.STAUS.RUNNABLE.name();
        this.nodeStatus = init;
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public FlowStatus getStatus() {
        return FlowStatus.of(flowStatus, nodeStatus);
    }

    @Override
    public void setStatus(FlowStatus status) {

    }
}
