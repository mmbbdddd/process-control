package io.ddbm.pc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RecordRepo {
    Map<Serializable, MockRecord> records = new HashMap<>();


    public static class MockRecord implements FlowRequest {
        Serializable id;
        String       status = "init";

        public MockRecord(Serializable id) {
            this.id = id;
        }

        @Override
        public String getNodeName() {
            return status;
        }

        @Override
        public Serializable getId() {
            return id;
        }
    }
}
