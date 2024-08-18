package cn.hz.ddbm.pc.support;

import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.StatusPair;
import cn.hz.ddbm.pc.core.Fsm;

import java.io.Serializable;
import java.util.UUID;

public class PayloadMock<S extends Enum<S>> implements FlowPayload<S> {
    String id;
    S nodeStatus;
    String flowStatus;

    public PayloadMock(S init) {
        this.id         = UUID.randomUUID().toString();
        this.flowStatus = Fsm.STAUS.RUNNABLE.name();
        this.nodeStatus = init;
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public StatusPair<S> getStatus() {
        return StatusPair.of(flowStatus, nodeStatus);
    }

    @Override
    public void setStatus(StatusPair<S> status) {

    }
}
