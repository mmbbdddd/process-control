package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Flow {
    String              name;
    FsmTable            fsmTable;
    Map<String, Node>   nodes;
    Map<String, Router> routers;
    String              sessionManager;
    String              statusManager;
    List<String>        plugins;
    Map<String, Object> attrs;

    public Flow(String name, List<Node> nodes, List<String> plugins, List<Router> routers, Map<String, Object> attrs) {
        Assert.notNull(name, "flow.name is null");
        Assert.notNull(nodes, "flow.nodes is null");
        plugins = null == plugins ? Collections.emptyList() : plugins;
        routers = null == routers ? Collections.emptyList() : routers;
        this.name = name;
        this.attrs = attrs;
        this.plugins = plugins;
        this.routers = routers.stream().collect(Collectors.toMap(Router::name, t -> t));
        this.nodes = nodes.stream().collect(Collectors.toMap(Node::getName, t -> t));
    }

    //初始化Flow的bean属性
    public Flow afterPropertiesSet() {
        Assert.isTrue(nodes.values().stream().filter(n -> n.type.equals(Node.Type.START)).count() == 1, "node.start count != 1");
        Assert.isTrue(nodes.values().stream().anyMatch(n -> n.type.equals(Node.Type.END)), "node.end count != 1");
        return this;
    }


    public Node getNode(String stepName) {
        return nodes.get(stepName);
    }

    /**
     * 1，获取当前节点状态，如无则为开始节点
     * 2，
     *
     * @param ctx
     */
    public <T> void execute(FlowContext<?> ctx) {
        Assert.isTrue(ctx != null, "ctx is null");
        String       node         = getOrInitNode(ctx);
        FsmRecord    atom         = fsmTable.onEvent(node, ctx.getEvent());
        AtomExecutor atomExecutor = null;
        ctx.setAtomExecutor(atomExecutor);
        atomExecutor.execute(ctx);
    }


    private String getOrInitNode(FlowContext<?> ctx) {
        return null;
    }

    public Node startStep() {
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


    @Data
    public static class FsmTable {
        List<FsmRecord> records;

        public FsmRecord onEvent(String step, String event) {
            return records.stream().filter(r -> Objects.equals(r.getStep(), step) && Objects.equals(r.getEvent(), event)).findFirst().orElse(null);
        }
    }

    @Data
    public static class FsmRecord {
        String step;
        String event;
        String action;
        String router;
    }


}
