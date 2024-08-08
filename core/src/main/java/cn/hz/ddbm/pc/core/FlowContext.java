package cn.hz.ddbm.pc.core;


import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Getter
public class FlowContext<T extends FlowEntity> {
    //    入参
    private String              event;
    private Map<String, Object> args;
    private Serializable        id;
    private T                   data;
    private Flow                flow;
    //    运行时数据
    @Setter
    private FlowStatus          status;
    @Setter
    private AtomExecutor        atomExecutor;


    public FlowContext(Flow flow, Serializable id, T data, String event, Map<String, Object> args) {
        this.event  = event;
        this.args   = args;
        this.id     = id;
        this.data   = data;
        this.flow   = flow;
        this.status = data.getStatus();
    }

//    public boolean isContinue() {
//        if (flow.getNode(status.getNode()).type == Node.Type.END) {
//            return false;
//        }
//        if (status.getFlow() != Flow.STAUS.RUNNABLE) {
//            return false;
//        }
//        if (InfraUtils.getMetricsWindows(status.getNode(), new Date()).getTimes() > flow.getNode(status.getNode()).retry) {
//            return false;
//        }
//        return true;
//    }

    public void metricsNode(String node) {
        InfraUtils.getNodeMetricsWindows(node, new Date()).incrementRetrys();
    }

    /**
     * 确保上下文状态一致
     * 1，status ==> entity
     */
    public void flush() {

    }
}
