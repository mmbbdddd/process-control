package cn.hz.ddbm.pc.status.dao;

import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.StatusPair;

public interface PayloadDao<T extends FlowPayload> {
    String flowName();

    void save(FlowPayload data);

    StatusPair get(String flow);
}
