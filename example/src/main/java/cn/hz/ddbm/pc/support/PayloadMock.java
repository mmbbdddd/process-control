package cn.hz.ddbm.pc.support;

import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.Node;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.Fsm;
import cn.hz.ddbm.pc.core.enums.FlowStatus;

import java.io.Serializable;
import java.util.UUID;

public class PayloadMock<S extends Enum<S>> implements FlowPayload<S> {
    String  id;
    Node<S> nodeStatus;

    public PayloadMock(S init) {
        this.id         = UUID.randomUUID().toString();
        this.nodeStatus = new Node<>(init, FlowStatus.INIT,Profile.chaosOf());
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Node<S> getStatus() {
        return nodeStatus;
    }

    @Override
    public void setStatus(Node<S> status) {

    }
}
