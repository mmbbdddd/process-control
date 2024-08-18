package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.enums.FlowStatus;
import cn.hz.ddbm.pc.core.exception.BlockedFlowException;
import cn.hz.ddbm.pc.core.exception.wrap.PauseFlowException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Node<S extends Enum<S>> {
    S name;

    FlowStatus type;

    Profile.StepAttrs attrs;

    public static <S extends Enum<S>> Node<S> cancel(S node) {
        return of(FlowStatus.CANCEL.name(), node);
    }

    public static <S extends Enum<S>> Node<S> finish(S node) {
        return of(FlowStatus.FINISH.name(), node);
    }

    public static <S extends Enum<S>> Node<S> blocked(BlockedFlowException e, S node) {
        return of(FlowStatus.INIT.name(), node);
    }

    public static <S extends Enum<S>> Node<S> pause(PauseFlowException e, S node) {
        return of(FlowStatus.PAUSE.name(), node);
    }

    public static <S extends Enum<S>> Node<S> of(S nodeStatus) {
        return of(FlowStatus.RUNNABLE.name(), nodeStatus);
    }

    public static <S extends Enum<S>> Node<S> of(String flowStatus, S nodeStatus) {
        return of(flowStatus, nodeStatus, null);
    }

    public static <S extends Enum<S>> Node<S> of(String flowStatus, S nodeStatus, Profile<S> profile) {
        Assert.notNull(flowStatus, "flowStatus is null");
        Assert.notNull(nodeStatus, "nodeStatus is null");
        return new Node<>(FlowStatus.valueOf(flowStatus), nodeStatus, profile);
    }


    public Node(FlowStatus type, S name, Profile<S> profile) {
        this.name  = name;
        this.type  = type;
        this.attrs = profile == null ? new Profile.StepAttrs(Profile.defaultOf()) : profile.getStepAttrsOrDefault(name);
    }

    public void on(FlowStatus.Event flowEvent) {
        this.type = this.type.on(flowEvent);
    }

    public void setState(S state) {
        this.name = state;
    }


    public Integer getRetry() {
        return attrs.getRetry();
    }

    public boolean isRunnable() {
        return (Objects.equals(type, FlowStatus.RUNNABLE) || Objects.equals(type, FlowStatus.INIT));
    }


}
