package io.ddbm.pc;

import io.ddbm.pc.factory.FlowFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Flows {

    @Autowired
    FlowFactory flowFactory;

    public FlowResponse execute(String flowName, FlowRequest request, String cmd) throws RouterException {
        Flow flow = flowFactory.get(flowName);
        return flow.execute(request, cmd);
    }
}
