package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Getter
@Slf4j
public class Node {
    final Type                type;
    final String              name;
    final Integer             retry;
    final Map<String, Object> attrs;

    //初始化Node的配置属性
    public Node(Type type, String name, Map<String, Object> attrs) {
        Assert.notNull(type, "type is null");
        Assert.notNull(name, "name is null");
        this.attrs = attrs;
        this.type = type == null ? Type.TASK : type;
        this.name = name;
        this.retry = (null != attrs && null != attrs.get("retry")) ? Integer.parseInt(attrs.get("retry").toString()) : 1;
    }


    public enum Type {
        START, TASK, END
    }
}
