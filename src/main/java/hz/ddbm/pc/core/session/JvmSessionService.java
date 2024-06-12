package hz.ddbm.pc.core.session;

import hz.ddbm.pc.core.domain.BizContext;
import hz.ddbm.pc.core.domain.Flow;
import hz.ddbm.pc.core.StatusService;
import hz.ddbm.pc.core.SessionService;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class JvmSessionService implements SessionService, StatusService {
    static String SESSION_KEY_FORMAT = "session:%s:%s:%s";
    static String STATUS_KEY_FORMAT  = "status:%s:%s";

    private ConcurrentHashMap<String, Object> session;

    public JvmSessionService() {
        this.session = new ConcurrentHashMap<>();
    }

    @Override
    public void put(Flow flow, Serializable id, String key, Object value) {
        this.session.put(String.format(SESSION_KEY_FORMAT, flow.getName(), id, key), value);
    }

    @Override
    public Object get(Flow flow, Serializable id, String key) {
        return this.session.get(String.format(SESSION_KEY_FORMAT, flow.getName(), id, key));
    }


    @Override
    public void updateNodeState(Flow flow, Serializable id, String node, BizContext ctx) {
        Assert.notNull(node, "node is null");
        String key = String.format(STATUS_KEY_FORMAT, flow.getName(), id);
//        if (!session.containsKey(key)) {
        session.put(key, new FlowNodeStatus(Flow.STAUS.RUNNABLE, node));
//        } else {
//            session.get
//        }
    }

    @Override
    public void updateFlowStatus(Flow flow, Serializable id, Flow.STAUS flowStatus, BizContext ctx) {
        String key = String.format(STATUS_KEY_FORMAT, flow.getName(), id);
        if (!session.containsKey(key)) {
            throw new RuntimeException("无此上下文");
        } else {
            FlowNodeStatus status = (FlowNodeStatus) session.get(key);
            session.put(key, new FlowNodeStatus(flowStatus, status.getNode()));
        }
    }

    @Override
    public FlowNodeStatus getStatus(Flow flow, Serializable id) {
        return null;
    }

}
