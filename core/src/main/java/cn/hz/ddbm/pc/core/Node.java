package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import lombok.Getter;


@Getter
public class Node implements State.Persist {
    final String            name;
    final Profile.StepAttrs attrs;

    //初始化Node的配置属性
    public Node(String name, Profile.StepAttrs attrs) {
        Assert.notNull(name, "name is null");
        this.attrs = attrs;
        this.name  = name;
    }

    @Override
    public String status() {
        return name;
    }

    @Override
    public Integer getRetry() {
        return attrs.getRetry();
    }

    @Override
    public String toString() {
        return name;
    }
}
