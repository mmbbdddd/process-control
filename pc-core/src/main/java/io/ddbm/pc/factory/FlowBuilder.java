package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import io.ddbm.pc.Node;
import io.ddbm.pc.Plugin;
import io.ddbm.pc.Router;
import io.ddbm.pc.router.ExpressionRouter;
import io.ddbm.pc.utils.Pair;
import io.ddbm.pc.utils.SpringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FlowBuilder {
    ArrayList<String> plugins;

    Map<String, ExpressionRouter> routers;

    Flow flow;

    public FlowBuilder(String name) {
        this.flow = new Flow(name);
        this.plugins = new ArrayList<>();
        this.routers = new HashMap<>();
    }

    public Node addNode(Node.Type  type,String name) {
         Node node = new Node(flow,type,name);
        this.flow.addNode(node);
        return node;
    }

    public void on(Node node, String event, String action, String to, String router, String desc, String retryCount) {
        node.on(event, action, to, router, desc, retryCount);

        if (Objects.nonNull(router)) {
            ExpressionRouter routerObj = Router.buildRouter(router);
            this.routers.put(routerObj.getRouterName(), routerObj);
        }
    }

    public void addPlugin(String name) {
        this.plugins.add(name);
    }

    public Flow build() {
        for (String plugin : plugins) {
            flow.getPlugins().add(SpringUtils.getBean(plugin, Plugin.class));
        }
        routers.forEach((routerName, router) -> {
            flow.getRouters().put(routerName, router);
        });
        return flow;
    }

    public void addCase(String routerName, String test, String to) {
        this.routers.get(routerName).getExpressions().add(Pair.of(test, to));
    }
}
