package io.ddbm.pc.router;

import io.ddbm.pc.Router;
import io.ddbm.pc.exception.RouterException;
import io.ddbm.pc.support.FlowContext;
import lombok.Getter;


public class NodeRouter implements Router {
    @Getter
    private String to;

    public NodeRouter(String to) {
        this.to = to;
    }

    @Override
    public String route(FlowContext ctx)
        throws RouterException {
        return to;
    }

    @Override
    public String name() {
        return "toNode:" + to;
    }
}
