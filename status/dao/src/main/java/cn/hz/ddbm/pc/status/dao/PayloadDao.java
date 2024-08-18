package cn.hz.ddbm.pc.status.dao;

import cn.hz.ddbm.pc.core.FlowPayload;

public interface PayloadDao<T extends FlowPayload> {
    String flowName();

    void save(FlowPayload data);

    FlowPayload get(String flow);
}
