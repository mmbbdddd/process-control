package cn.hz.ddbm.pc.newcore.config;

import cn.hz.ddbm.pc.newcore.FlowAttrs;

import java.util.HashMap;
import java.util.Map;

public class JvmProperties {
    public static Map<String, FlowAttrs> flowAttrs;

    static {
        flowAttrs = new HashMap<>();
    }
}
