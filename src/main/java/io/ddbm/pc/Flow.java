package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger                digest = LoggerFactory.getLogger("digest");
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
    public void execute(FlowRequest request, String event) throws PauseException, InterruptException {
        try {
            FlowContext ctx = new FlowContext(this, request, event);
            ctx.getEvent().execute(ctx);
        } catch (PauseException e) {
            FlowContext ctx = e.getCtx();
            digest.info("flow:{},id:{},from:{},to:{}", name, request.getId(), ctx.getNode().getName(), ctx.getRequest().getStatus());
            throw e;
        } catch (InterruptException e) {
            digest.error("flow:{},id:{},from:{},error:", name, request.getId(), e.getNode(), e);
            throw e;
        }
    }

    public TaskNode nodeOf(String node) throws InterruptException {
        if (StringUtils.isEmpty(node)) {
            return startNode();
        } else {
            if (!nodes.containsKey(node)) {
                throw InterruptException.noSuchNode(node);
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
