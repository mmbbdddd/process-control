package cn.hz.ddbm.pc.core;


import cn.hutool.core.lang.Assert;
import lombok.Data;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 21:57
 * @Version 1.0.0
 **/


@Data
public class FlowStatus {
    Flow.STAUS flow;
    String     node;

    public static FlowStatus pause(String node) {
        Assert.notNull(node, "nodeStatus is null");
        FlowStatus status = new FlowStatus();
        status.node = node;
        status.flow = Flow.STAUS.PAUSE;
        return status;
    }

    public static FlowStatus of(String node) {
        Assert.notNull(node, "nodeStatus is null");
        FlowStatus status = new FlowStatus();
        status.node = node;
        status.flow = Flow.STAUS.RUNNABLE;
        return status;
    }

    public static FlowStatus of(String flowStatus, String nodeStatus) {
        Assert.notNull(flowStatus, "flowStatus is null");
        Assert.notNull(nodeStatus, "nodeStatus is null");
        FlowStatus status = new FlowStatus();
        status.node = nodeStatus;
        status.flow = Flow.STAUS.valueOf(flowStatus);
        return status;
    }

    @Override
    public String toString() {
        return "FlowStatus{" +
                "flow=" + flow +
                ", node='" + node + '\'' +
                '}';
    }
}
