package hz.ddbm.pc.core.service.session;

import hz.ddbm.pc.core.fsm.core.BizContext;
import hz.ddbm.pc.core.fsm.core.Flow;
import hz.ddbm.pc.core.service.StatusService;
import hz.ddbm.pc.core.service.SessionService;

import java.io.Serializable;

public class RedisSessionService implements SessionService, StatusService {
    @Override
    public void updateNodeState(Flow flow, Serializable id, String nodeStatus, BizContext ctx) {

    }

    @Override
    public void updateFlowStatus(Flow flow, Serializable id, Flow.STAUS flowStatus, BizContext ctx) {

    }

    @Override
    public FlowNodeStatus getStatus(Flow flow, Serializable id) {
        return null;
    }


    @Override
    public void put(Flow flow, Serializable id, String key, Object value) {

    }

    @Override
    public Object get(Flow flow, Serializable id, String key) {
        return null;
    }
}
