package hz.ddbm.pc.core.fsm.core;

import hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class BizContext<T> {
    //    入参
    private String              event;
    private Map<String, Object> args;
    //    实体
    private Serializable        id;
    private T                   data;
    private Flow                flow;
    //    运行时数据
    @Setter
    private String              node;
    @Setter
    private Transition          transition;
    @Setter
    private Object              actionResult;
    @Setter
    private Throwable           actionError;
    @Setter
    private Flow.STAUS          flowStatus;

    ConcurrentHashMap<String, AtomicInteger> executeCounts;


    public BizContext(Flow flow, Serializable id, T data, String event, Map<String, Object> args) {
        this.event = event;
        this.args = args;
        this.id = id;
        this.data = data;
        this.flow = flow;
        this.flowStatus = Flow.STAUS.RUNNABLE;
        this.executeCounts = new ConcurrentHashMap<>();
        flow.nodes.forEach((nodeName, node) -> {
            this.executeCounts.put(nodeName, new AtomicInteger(1));
        });
    }

    public void setActionError(Exception actionError) {
        this.actionError = actionError;
    }

    public void setSession(String key, Object value) {
        InfraUtils.getSessionService().put(flow, id, key, value);
    }

    public void getSession(String key) {
        InfraUtils.getSessionService().get(flow, id, key);
    }

    public boolean isContinue() {
        if (flow.getNode(node).type == Node.Type.END) {
            return false;
        }
        if (flowStatus != Flow.STAUS.RUNNABLE) {
            return false;
        }
        if (executeCounts.get(node).get() > 10) {
            return false;
        }
        return true;
    }

    public void increment(String node) {
        executeCounts.get(node).incrementAndGet();
    }
}
