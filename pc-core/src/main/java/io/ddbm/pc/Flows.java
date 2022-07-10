package io.ddbm.pc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Flows {
    static ConcurrentMap<String, Flow> flows = new ConcurrentHashMap<>();

    public static void set(Flow flow) {
        if (flows.containsKey(flow.name)) {
            flows.remove(flow.name);
        }
        flows.put(flow.name, flow);
    }

    public static Flow get(String flowName) {
        return flows.get(flowName);
    }

}
