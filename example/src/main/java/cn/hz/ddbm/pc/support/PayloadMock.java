package cn.hz.ddbm.pc.support;

import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.State;
import cn.hz.ddbm.pc.core.enums.FlowStatus;

import java.io.Serializable;
import java.util.UUID;

public class PayloadMock<S extends Enum<S>> implements FlowPayload<S> {
    String   id;
    State<S> nodeStatus;

    public PayloadMock(S init) {
        this.id         = UUID.randomUUID().toString();
        this.nodeStatus = new State<>(init, FlowStatus.INIT);
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public State<S> getStatus() {
        return nodeStatus;
    }

    @Override
    public void setStatus(State<S> status) {

    }
}
