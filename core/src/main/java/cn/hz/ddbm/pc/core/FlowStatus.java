package cn.hz.ddbm.pc.core;


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

    public static FlowStatus of(String node) {
        FlowStatus status = new FlowStatus();
        status.node = node;
        status.flow = Flow.STAUS.RUNNABLE;
        return status;
    }
}