package io.ddbm.pc;

import org.springframework.util.Assert;

public class Start extends _Node {
    public Start(String name, NameRouter router) {
        super(name);
        Command cmd = new Command(Flow.DEFAULT_COMMAND, router);
        cmd.setNode(this);
        this.cmds.put(Flow.DEFAULT_COMMAND, cmd);
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(name);
    }
}