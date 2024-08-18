package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.enums.FlowStatus;
import cn.hz.ddbm.pc.core.exception.BlockedFlowException;
import cn.hz.ddbm.pc.core.exception.wrap.PauseFlowException;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;

@Getter
public class Node<S extends Enum<S>> {
    S                 name;
    FlowStatus        type;
    Profile.StepAttrs attrs;


    public Node(S name, FlowStatus type, Profile<S> profile) {
        this.name  = name;
        this.type  = type;
        this.attrs = profile.getStepAttrsOrDefault(name);
    }

    public void on(FlowStatus.Event flowEvent) {
        this.type = this.type.on(flowEvent);
    }


    public Integer getRetry() {
        return attrs.getRetry();
    }

    public boolean isRunnable() {
        return (Objects.equals(type, FlowStatus.RUNNABLE) || Objects.equals(type, FlowStatus.INIT));
    }


    public void flush(S state) {
        this.name = state;
    }
}
