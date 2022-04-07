package io.ddbm.pc;

import org.springframework.util.Assert;

public class End extends _Node {
    public End(String name) {
        super(name);
    }

    @Override
    public _Node onFail(FlowContext ctx, Exception e) throws RouterException {
        //TODO log
        return this;
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(name);
    }

}