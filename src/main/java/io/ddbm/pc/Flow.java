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
    Logger             logger = LoggerFactory.getLogger(getClass());
    Logger             digest = LoggerFactory.getLogger("digest");
    String             name;
    Map<String, Task>  nodes;
    LinkedList<Plugin> plugins;

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
    public void execute(FlowRequest request, String event, Pc.FlowResultListener listener)   {
        Assert.notNull(request, "request is null");
        event = StringUtils.isEmpty(event) ? Coast.DEFAULT_EVENT : event;
        try {
            FlowContext ctx = new FlowContext(this, request, event);
            ctx.getEvent().execute(ctx);
            digest.info("flow:{},id:{},from:{},event:{},action:{},to:{}", name, request.getId(), ctx.getNode().getName(), event, ctx.getEvent().getActionName(), ctx.getRequest().getStatus());
            listener.onResult(ctx);
        } catch (PauseException e) {
            FlowContext ctx = e.getCtx();
            digest.warn("flow:{},id:{},from:{},event:{},action:{},pause:{}", name, request.getId(), ctx.getNode().getName(), event, ctx.getEvent().getActionName(), e.getMessage());
            logger.warn("", e);
            listener.onPauseException(e);
        } catch (InterruptException e) {
            digest.error("flow:{},id:{},from:{},event:{},error:{}", name, request.getId(), e.getNode(), event, e.getMessage());
//            logger.warn("", e);
            throw e;
        }
    }

    public Task nodeOf(String node) throws InterruptException {
        if (StringUtils.isEmpty(node)) {
            return startNode();
        } else {
            if (!nodes.containsKey(node)) {
                throw InterruptException.noSuchNode(node);
            }
            return nodes.get(node);
        }
    }

    public void addNode(Task node) {
        this.nodes.put(node.name, node);
    }

    private Task startNode() {
        return nodes.values().stream().filter(t -> t.getType().equals(Task.Type.START)).findFirst().get();
    }

    public Task getNode(String node) {
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
