package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptedException;
import io.ddbm.pc.exception.PauseException;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;


/**
 * 流程定义
 */
@Getter
public class Flow {
    String                name;
    Map<String, TaskNode> nodes;
    LinkedList<Plugin>    plugins;

    public Flow(String name) {
        Assert.notNull(name, "工作流名称为空");
        this.name    = name;
        this.nodes   = new HashMap<>();
        this.plugins = new LinkedList<>();
    }

    public String getName() {
        return name;
    }


    /**
     * 单步执行
     */
    public void execute(FlowRequest request, String event) throws InterruptedException, PauseException {
        FlowContext ctx = new FlowContext(this, request, event);
//        获取当前数据节点
        ctx.getEvent().execute(ctx);
    }

    public TaskNode nodeOf(String node) throws InterruptedException {
        if (StringUtils.isEmpty(node)) {
            return startNode();
        } else {
            if (!nodes.containsKey(node)) {
                throw InterruptedException.noSuchNode(node);
            }
            return nodes.get(node);
        }
    }

    public void addNode(TaskNode node) {
        this.nodes.put(node.name, node);
    }

    private TaskNode startNode() {
        return nodes.values().stream().filter(t -> t.getType().equals(TaskNode.Type.START)).findFirst().get();
    }

    public TaskNode getNode(String node) {
        return nodes.get(node);
    }


    public LinkedList<Plugin> getPlugins() {
        return plugins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flow flow = (Flow) o;
        return Objects.equals(name, flow.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
