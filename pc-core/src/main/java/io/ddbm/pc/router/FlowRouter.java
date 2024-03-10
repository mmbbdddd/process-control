package io.ddbm.pc.router;

import io.ddbm.pc.Router;
import io.ddbm.pc.exception.RouterException;
import io.ddbm.pc.support.FlowContext;


public class FlowRouter implements Router {
    String to;

    public FlowRouter(String to) {
        this.to = to;
    }

    @Override
    public String route(FlowContext ctx)
        throws RouterException {
        return to;
    }

    @Override
    public String name() {
        return "toFlow:" + to;
    }
}
