package io.ddbm.pc.config;

import io.ddbm.pc.Action;
import io.ddbm.pc.support.FlowContext;


public class NoneAction implements Action {
    String name;

    @Override
    public String name() {
        return name;
    }

    @Override
    public void run(FlowContext ctx) {

    }
 
}
