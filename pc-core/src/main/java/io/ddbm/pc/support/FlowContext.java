package io.ddbm.pc.support;

import io.ddbm.pc.Event;
import io.ddbm.pc.Flow;
import io.ddbm.pc.FlowRequest;
import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.Node;
import io.ddbm.pc.RunMode;
import io.ddbm.pc.chaos.ChaosFlowRequest;
import io.ddbm.pc.config.Coast;
import io.ddbm.pc.exception.ContextCreateException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.utils.Pair;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Getter
public abstract class FlowContext {

    @NonNull
    private final RunMode runMode;

    @NonNull
    private final FlowRequest<?> request;

    @NonNull
    private final Flow flow;

    @NonNull
    private final Map<String, Object> session;

    @NonNull
    private Pair<FlowStatus, String> status;

    @NonNull
    private Event event;

    @Setter
    private Throwable actionError;

    public FlowContext(
        RunMode runMode, FlowRequest<?> request, Flow flow, Pair<FlowStatus, String> status,
        Map<String, Object> session)
        throws ContextCreateException {
        Assert.notNull(request, "request is null");
        this.runMode = runMode;
        this.request = request;
        this.flow = flow;
        this.status = status;

        if (null == session) {
            this.session = new HashMap<>();
        } else {
            this.session = session;
        }
        if (request instanceof ChaosFlowRequest) {
            ((ChaosFlowRequest)request).setFlowContext(this);
        }
        try {
            this.event = flow.nodeOf(status.getValue()).getEvent(request.getEvent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ContextCreateException(request, status, e);
        }

    }

    public Boolean isRun() {
        return runMode.isRun(this);
    }

    public Node node()
        throws NoSuchNodeException {
        if (StringUtils.isEmpty(status.getValue())) {
            return flow.startNode();
        } else {
            if (!flow.getNodes().containsKey(status.getValue())) {
                throw new NoSuchNodeException(status.getValue(), status.getValue());
            }
            return flow.getNodes().get(status.getValue());
        }
    }

    public String getNode() {
        return status.getValue();
    }

    public void setFlowStatus(FlowStatus flowStatus) {
        this.status = Pair.of(flowStatus, this.status.getValue());
    }

    public void updateNodeStatus(String targetNode) {
        this.status = Pair.of(this.status.getKey(), targetNode);
        try {
            if (Objects.equals(Node.Type.END, node().getType())) {
                setFlowStatus(FlowStatus.DONE);
            }
        } catch (NoSuchNodeException e) {
            //无此节点，流程有bug，设置状态为暂停。
            e.printStackTrace();
            setFlowStatus(FlowStatus.PAUSE_BUG);
        }
    }

    public void refreshEvent() {
        try {
            if (isRun()) {
                this.event = node().getEvent(Coast.DEFAULT_EVENT);
            }
        } catch (NoSuchEventException e) {
            //如无此命令，以为着流程设计存在bug，流程暂停。
            setFlowStatus(FlowStatus.PAUSE_BUG);
            flow.onNoSuchEventException(e, this);
        } catch (NoSuchNodeException e) {
            //如无此节点，以为着流程设计存在bug，流程暂停。
            setFlowStatus(FlowStatus.PAUSE_BUG);
            flow.onNoSuchNodeException(e, this);
        }
    }

}
