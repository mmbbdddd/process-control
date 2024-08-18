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
public class FlowStatus<S extends Enum<S>> {
    Fsm.STAUS flow;
    S         node;

    public static <S extends Enum<S>> FlowStatus<S> pause(S node) {
        Assert.notNull(node, "nodeStatus is null");
        FlowStatus<S> status = new FlowStatus<>();
        status.node = node;
        status.flow = Fsm.STAUS.PAUSE;
        return status;
    }

    public static <S extends Enum<S>> FlowStatus<S> of(S node) {
        Assert.notNull(node, "nodeStatus is null");
        FlowStatus<S> status = new FlowStatus<>();
        status.node = node;
        status.flow = Fsm.STAUS.RUNNABLE;
        return status;
    }

    public static <S extends Enum<S>> FlowStatus<S> of(String flowStatus, S nodeStatus) {
        Assert.notNull(flowStatus, "flowStatus is null");
        Assert.notNull(nodeStatus, "nodeStatus is null");
        FlowStatus<S> status = new FlowStatus<>();
        status.node = nodeStatus;
        status.flow = Fsm.STAUS.valueOf(flowStatus);
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
