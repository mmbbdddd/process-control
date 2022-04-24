package io.ddbm.pc;

import org.springframework.util.Assert;

public class Node extends _Node {

    public Node(String name) {
        super(name);
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(name);
    }


    public void addCommand(Command command) {
        this.cmds.put(command.cmd, command);
        command.setNode(this);
    }
}