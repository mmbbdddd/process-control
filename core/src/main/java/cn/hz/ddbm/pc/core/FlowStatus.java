package cn.hz.ddbm.pc.core;


import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.exception.BlockedFlowException;
import cn.hz.ddbm.pc.core.exception.wrap.PauseFlowException;
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

    public static <S extends Enum<S>> FlowStatus<S> cancel(S node) {
        return of(Fsm.STAUS.FINISH.name(), node);
    }

    public static <S extends Enum<S>> FlowStatus<S> finish(S node) {
        return of(Fsm.STAUS.FINISH.name(), node);
    }

    public static <S extends Enum<S>> FlowStatus<S> blocked(BlockedFlowException e, S node) {
        return of(Fsm.STAUS.BLOCKED.name(), node);
    }

    public static <S extends Enum<S>> FlowStatus<S> pause(PauseFlowException e, S node) {
        return of(Fsm.STAUS.PAUSE.name(), node);
    }

    public static <S extends Enum<S>> FlowStatus<S> of(S node) {
        return of(Fsm.STAUS.RUNNABLE.name(), node);
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
