package io.ddbm.pc;

import java.io.Serializable;
import java.util.Map;

public interface FlowRecordRepository {
    void persist(FlowContext ctx, String targetNodeName);

    FLowRecord get(Serializable id);

    FLowRecord newRecord( Map<String, Object> args);

    String flowName();
}
