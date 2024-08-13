package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import lombok.Getter;

import java.util.Map;


@Getter
public class Node implements Step.Persist {
    final String              name;
    final Integer             retry;
    final Map<String, Object> attrs;

    //初始化Node的配置属性
    public Node( String name, Map<String, Object> attrs) {
        Assert.notNull(name, "name is null");
        this.attrs = attrs;
        this.name  = name;
        this.retry = (null != attrs && null != attrs.get("retry")) ? Integer.parseInt(attrs.get("retry")
                .toString()) : 1;
    }

    @Override
    public String status() {
        return name;
    }


}
