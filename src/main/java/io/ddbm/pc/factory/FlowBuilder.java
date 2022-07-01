package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import io.ddbm.pc.Plugin;
import io.ddbm.pc.TaskNode;
import io.ddbm.pc.utils.SpringUtils;

import java.util.ArrayList;

public class FlowBuilder {
    ArrayList<String> plugins;
    Flow              flow;

    public FlowBuilder(String name) {
        this.flow    = new Flow(name);
        this.plugins = new ArrayList<>();
    }

    public Flow build() {
        for (String plugin : plugins) {
            flow.getPlugins().add(SpringUtils.getBean(plugin, Plugin.class));
        }
        return flow;
    }

    public void addNode(TaskNode node) {
        this.flow.addNode(node);
    }

    public void addPlugin(String name) {
        this.plugins.add(name);
    }
}
