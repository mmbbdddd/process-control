package cn.hz.ddbm.pc.newcore.saga;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.State;
import lombok.Data;

import java.util.Objects;

@Data
public class SagaState implements State {
    public FlowStatus        flowStatus;
    public Integer           index;
    public SagaWorker.Offset offset;

    public SagaState(Integer index, SagaWorker.Offset offset) {
        this(FlowStatus.RUNNABLE, index, offset);
    }

    public SagaState(FlowStatus flowStatus, Integer index, SagaWorker.Offset offset) {
        Assert.notNull(flowStatus, "status is null");
        Assert.notNull(index, "index is null");
        Assert.notNull(offset, "offset is null");
        this.flowStatus = flowStatus;
        this.index      = index;
        this.offset     = offset;
    }

    @Override
    public String code() {
        return index + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SagaState sagaState = (SagaState) o;
        return Objects.equals(index, sagaState.index);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(index);
    }
}
