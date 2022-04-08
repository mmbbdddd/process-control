package io.ddbm.pc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MockFlowRecordRepository implements FlowRecordRepository {
    Map<Serializable, MockFlowRecord> records = new HashMap<>();

    @Override
    public void persist(FlowContext ctx, String targetNodeName) {
        MockFlowRecord r = records.get(ctx.getId());
        r.status = targetNodeName;
    }

    @Override
    public FLowRecord get(Serializable id) {
        return records.get(id);
    }

    @Override
    public FLowRecord newRecord(Map<String, Object> args) {
        Serializable id = Double.valueOf(Math.random() * 10).intValue();
        records.put(id, new MockFlowRecord(id));
        return records.get(id);
    }

    @Override
    public String flowName() {
        return "simple";
    }

    class MockFlowRecord implements FLowRecord {
        Serializable id;
        String       status = "init";

        public MockFlowRecord(Serializable id) {
            this.id = id;
        }

        @Override
        public String translateStatusToNode() {
            return status;
        }

        @Override
        public Serializable getId() {
            return id;
        }
    }
}
