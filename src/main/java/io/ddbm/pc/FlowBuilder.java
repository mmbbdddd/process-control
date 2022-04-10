package io.ddbm.pc;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlowBuilder {
    Flow                     flow;
    String                   contextService;
    String                   startNodeName;
    String                   startNodeAction;
    String                   startNodeTo;
    Map<String, NodeBuilder> nodeBuilders;

    public FlowBuilder(String name, String contextService) {
        flow                = new Flow(name);
        this.contextService = contextService;
        nodeBuilders        = new HashMap<>();

    }

    public FlowBuilder addStartNode(String nodeName, String actionName, String to) {
        this.startNodeName   = nodeName;
        this.startNodeAction = actionName;
        this.startNodeTo     = to;
        return this;
    }

    public FlowBuilder addEndNode(End node) {
        flow.addNode(node);
        return this;
    }

    public FlowBuilder onCmd(String nodeName, String cmd, String actionName, Router.Type type, String router, String failNode) {
        getOrNewNodeBuilder(nodeName).onCmd(cmd, actionName, type, router, failNode);
        return this;
    }

    public FlowBuilder addRouter(String routerName, LinkedHashMap<String, String> expression) {
        ExpressionRouter router = new ExpressionRouter(routerName, expression);
        flow.addRouter(router);
        return this;
    }


    public Flow build(ApplicationContext ctx) throws Exception {
        Assert.notNull(startNodeName, "startNode name is null");
        Assert.notNull(startNodeAction, "startNode action is null");
        Assert.notNull(startNodeTo, "startNode to is null");
        Start start = new Start(startNodeName, new NameRouter(startNodeTo));
        start.setFlow(flow);
        flow.addNode(start);
        for (Map.Entry<String, NodeBuilder> entry : this.nodeBuilders.entrySet()) {
            String      nodeName = entry.getKey();
            NodeBuilder nb       = entry.getValue();
            flow.addNode(nb.build(ctx, flow));
        }
        flow.contextService = getContextServiceOfFlow(ctx, contextService);
        flow.afterPropertiesSet();
        start.afterPropertiesSet();
        return flow;
    }

    private ContextService getContextServiceOfFlow(ApplicationContext ctx, String flow) {
        return ctx.getBean(contextService, ContextService.class);
    }


    private NodeBuilder getOrNewNodeBuilder(String nodeName) {
        if (!nodeBuilders.containsKey(nodeName)) {
            nodeBuilders.put(nodeName, new NodeBuilder(nodeName));
        }
        return nodeBuilders.get(nodeName);
    }


    class NodeBuilder {
        String                      nodeName;
        Map<String, CommandBuilder> cmdBuilders;

        public NodeBuilder(String nodeName) {
            this.nodeName    = nodeName;
            this.cmdBuilders = new HashMap<>();
        }

        public void onCmd(String cmd, String actionName, Router.Type type, String router, String failNode) {
//            getOrNewCommandBuilder(cmd,actionName,type,router,failNode);
            if (!cmdBuilders.containsKey(cmd)) {
                if (type == Router.Type.NAME) {
                    cmdBuilders.put(cmd, CommandBuilder.direcTo(cmd, router, failNode));
                } else {
                    cmdBuilders.put(cmd, CommandBuilder.expression(cmd, actionName, router, failNode));
                }
            }
        }

        public Node build(ApplicationContext ctx, Flow flow) throws Exception {
            Node node = new Node(nodeName);
            node.setFlow(flow);
            for (Map.Entry<String, CommandBuilder> entry : cmdBuilders.entrySet()) {
                String         cmd = entry.getKey();
                CommandBuilder cb  = entry.getValue();
                node.addCommand(cb.build(ctx, node));
            }
            node.afterPropertiesSet();
            return node;
        }
    }

    static class CommandBuilder {
        String      cmd;
        String      actionName;
        Router.Type type;
        String      router;
        String      failNode;

        public static CommandBuilder direcTo(String cmd, String targetNode, String failNode) {
            CommandBuilder b = new CommandBuilder();
            b.cmd      = cmd;
            b.type     = Router.Type.NAME;
            b.router   = targetNode;
            b.failNode = failNode;
            return b;
        }

        public static CommandBuilder expression(String cmd, String actionName, String router, String failNode) {
            CommandBuilder b = new CommandBuilder();
            b.cmd        = cmd;
            b.actionName = actionName;
            b.type       = Router.Type.EXPRESSION;
            b.router     = router;
            b.failNode   = failNode;
            return b;
        }

        public Command build(ApplicationContext ctx, _Node node) throws Exception {
            if (type.equals(Router.Type.NAME)) {
                Command command = new Command(cmd, new NameRouter(router),failNode);
                command.setNode(node);
                command.afterPropertiesSet();
                return command;
            } else {
                Command command = new Command(cmd, ctx.getBean(actionName, Action.class), node.flow.getRouter(router),failNode);
                command.setNode(node);
                command.afterPropertiesSet();
                return command;
            }
        }
    }
}
