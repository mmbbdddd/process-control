package hz.ddbm.pc.core.fsm.core;

import hz.ddbm.pc.core.config.FlowProperties;
import hz.ddbm.pc.core.service.SessionService;
import hz.ddbm.pc.core.service.StatusService;
import hz.ddbm.pc.core.service.session.FlowNodeStatus;
import hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Flow {
    public static final String STATUS_PAUSE = "pause";
    String              name;
    FlowProperties      attrs;
    Map<String, Node>   nodes;
    List<Plugin>        plugins;
    Map<String, Router> routers;
    StatusService statusService;
    SessionService sessionService;

    public Flow(FlowProperties attrs, List<Node> nodes, List<Plugin> plugins, List<Router> routers) {
        Assert.notNull(attrs.getName(), "flow.name is null");
        Assert.notNull(nodes, "flow.nodes is null");
        plugins = null == plugins ? Collections.emptyList() : plugins;
        routers = null == routers ? Collections.emptyList() : routers;
        this.name = attrs.getName();
        this.attrs = attrs;
        this.plugins = plugins;
        this.routers = routers.stream().collect(Collectors.toMap(Router::getName, t -> t));
        this.nodes = nodes.stream().collect(Collectors.toMap(Node::getName, t -> t));
    }

    //初始化Flow的bean属性
    public Flow init() {
        this.statusService = InfraUtils.getStatusService(this.attrs.getStatusService());
        this.sessionService = InfraUtils.getSessionService();
        this.nodes.forEach((nodeName, node) -> {
            node.init(this);
        });
        Assert.isTrue(nodes.values().stream().filter(n -> n.type.equals(Node.Type.START)).count() == 1, "node.start count != 1");
        Assert.isTrue(nodes.values().stream().anyMatch(n -> n.type.equals(Node.Type.END)), "node.end count != 1");
        return this;
    }


    public Node getNode(String nodeName) {
        return nodes.get(nodeName);
    }

    /**
     * 1，获取当前节点状态，如无则为开始节点
     * 2，
     *
     * @param ctx
     */
    public <T> void execute(BizContext<T> ctx) {
        Assert.notNull(ctx, "BizContext is null");
        Node node = null;
        if (ctx.getNode() == null) {
            FlowNodeStatus status = statusService.getStatus(ctx.getFlow(), ctx.getId());
            if (StringUtils.isEmpty(status)) {
                ctx.setNode(startNode().name);
            }
            node = ctx.getFlow().getNode(ctx.getNode());
            Assert.notNull(ctx.getNode(), "no such node:" + status);
        } else {
            node = ctx.getFlow().getNode(ctx.getNode());
        }
        node.onEvent(ctx.getEvent(), ctx);

    }

    public Node startNode() {
        return nodes.values().stream().filter(t -> t.type.equals(Node.Type.START)).findFirst().get();
    }

    public Router getRouter(String routerName) {
        return routers.get(routerName);
    }

    public Set<String> nodeNames() {
        return nodes.keySet();
    }


    public enum STAUS {
        RUNNABLE, PAUSE, CANCEL
    }


}
