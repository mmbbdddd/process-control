package io.ddbm.pc.chaos;

import io.ddbm.pc.FlowRequest;
import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.config.Coast;
import io.ddbm.pc.factory.FlowFactory;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;
import io.ddbm.pc.utils.SpringUtils;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;


public class ChaosFlowRequest extends FlowRequest<Map<String, Object>> {
    @Setter
    FlowContext flowContext;

    public ChaosFlowRequest(
        @NonNull String flowName, Long snakeId, Map<String, Object> data) {
        super(flowName, Coast.DEFAULT_EVENT, snakeId, data);
    }

    @Override
    public @NonNull String getEvent() {
        Assert.notNull(flowContext, "flowContext is null");
        return SpringUtils.getBean(ChaosService.class).produceEvent(getFlowName(), flowContext.getNode());
    }

    public void setDateId(Serializable uuid) {
        this.dateId = uuid;
    }

    @Override
    public Pair<FlowStatus, String> getStatus(Map<String, Object> data) {
        Object flowStatus = data.get(Coast.FLOW_STATUS_FIELD);
        Object nodeStatus = data.get(Coast.FLOW_STATUS_FIELD);
        return Pair.of(flowStatus == null ? FlowStatus.INIT : FlowStatus.valueOf(flowStatus.toString()),
            nodeStatus == null ? FlowFactory.get(flowName).startNode().getName() : nodeStatus.toString());

    }

    @Override
    public Map<String, Object> setStatus(Pair<FlowStatus, String> status) {
        return null;
    }
}
