package cn.hz.ddbm.pc.newcore.fsm;

import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.State;
import lombok.Data;

import java.util.Objects;

@Data
public class FsmState implements State {
    public FlowStatus       flowStatus;
    public Enum             state;
    public FsmWorker.Offset offset;

    public FsmState(Enum state, FsmWorker.Offset offset) {
        this(FlowStatus.RUNNABLE, state, offset);
    }

    public FsmState(FlowStatus flowStatus, Enum state, FsmWorker.Offset offset) {
        this.flowStatus = flowStatus;
        this.state      = state;
        this.offset     = offset;
    }

    @Override
    public String code() {
        return state.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FsmState fsmState = (FsmState) o;
        return Objects.equals(state, fsmState.state);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(state);
    }
}
