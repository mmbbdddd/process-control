package cn.hz.ddbm.pc.core;

import lombok.Getter;

@Getter
public class Node<S extends Enum<S>> {
    S name;

    Type type;

    Profile.StepAttrs attrs;

    public Node(Type type, S name, Profile profile) {
        this.name  = name;
        this.type  = type;
        this.attrs = profile == null ? new Profile.StepAttrs(Profile.defaultOf()) : profile.getStepAttrsOrDefault(name);
    }

    public Integer getRetry() {
        return attrs.getRetry();
    }

    public enum Type {
        START, TASK, END
    }

}
