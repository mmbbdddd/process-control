package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import lombok.Getter;

@Getter
public class Node implements State.Persist {
    String name;

    Type type;

    Profile.StepAttrs attrs;

    public Node(Type type, String name, Profile.StepAttrs attrs,Profile profile) {
        this.name  = name;
        this.type  = type;
        this.attrs = attrs==null?new Profile.StepAttrs(profile):attrs;
    }

    @Override
    public String status() {
        return name;
    }

    @Override
    public Integer getRetry() {
        return attrs.getRetry();
    }

   public enum Type {
        START, TASK, END
    }

}
