package hz.ddbm.pc.core.factory.fsm;

import hz.ddbm.pc.core.config.FlowProperties;
import hz.ddbm.pc.core.config.NodeProperties;
import hz.ddbm.pc.core.config.TransitionProperties;
import hz.ddbm.pc.core.domain.*;
import hz.ddbm.pc.core.utils.InfraUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public abstract class Fsm {
    public abstract Flow flow();


    public static class FlowBuilder {
        String            name;
        String            statusService;
        String            sessionProvider;
        List<NodeBuilder> nodes;
        List<Router>      routers;
        List<String>      plugin;

        public FlowBuilder(String name, String statusService, String sessionProvider) {
            this.name = name;
            this.statusService = statusService;
            this.sessionProvider = sessionProvider;
            this.nodes = new ArrayList<>();
            this.routers = new ArrayList<>();
            this.plugin = new ArrayList<>();
        }

        public void addNode(NodeBuilder node) {
            this.nodes.add(node);
        }

        public void addRouter(Router router) {
            this.routers.add(router);
        }

        public void addPlugin(String plugin) {
            this.plugin.add(plugin);
        }

        public Flow build() {
            List<Node>   nodes2   = nodes.stream().map(NodeBuilder::toNode).collect(Collectors.toList());
            List<Plugin> plugins2 = plugin.stream().map(plugin -> InfraUtils.getBean(plugin, Plugin.class)).collect(Collectors.toList());
            return new Flow(new FlowProperties(name, statusService, sessionProvider), nodes2, plugins2, routers).init();
        }
    }

    public static class NodeBuilder {
        Node.Type        type;
        String           name;
        List<Transition> events;

        public NodeBuilder(Node.Type type, String name) {
            this.type = type;
            this.name = name;
            this.events = new ArrayList<>();
        }

        public NodeBuilder onEvent(String event, String action, String router) {
            return onEvent(event, action, router, null, null);
        }

        public NodeBuilder onEvent(String event, String action, String router, String failToNode, String saga) {
            this.events.add(new Transition(new TransitionProperties(event, action, router, failToNode, saga)));
            return this;
        }

        public Node toNode() {
            return new Node(new NodeProperties(type, name), events);
        }
    }


}

