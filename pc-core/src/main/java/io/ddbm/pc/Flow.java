package io.ddbm.pc;

import io.ddbm.pc.exception.ContextCreateException;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.MockException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.NonRunnableException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.SessionException;
import io.ddbm.pc.status.StatusException;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 工作流定义
 */
@Getter
@Slf4j
public class Flow {

    String name;

    Map<String, Node> nodes;

    List<Plugin> plugins;

    Map<String, Router> routers;

    public Flow(String name) {
        Assert.notNull(name, "工作流名称为空");
        this.name = name;
        this.nodes = new HashMap<>();
        this.plugins = new ArrayList<>();
        this.routers = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    /**
     * 单步执行
     */
    public void execute(FlowContext ctx)
        throws PauseException, InterruptException, ParameterException, NonRunnableException, NoSuchNodeException,
               NoSuchEventException {
        Assert.notNull(ctx, "ctx is null");
        if (ctx.isRun()) {
            Event event = ctx.getEvent();
            //            执行事件 
            try {
                event.execute(ctx);
            } catch (MockException e) {
                //                e.printStackTrace();
                //模拟异常 忽略，继续
            } 
        } else {
            throw new NonRunnableException();
        }
    }

    public Node nodeOf(String node)
        throws NoSuchNodeException {
        if (StringUtils.isEmpty(node)) {
            return startNode();
        } else {
            if (!getNodes().containsKey(node)) {
                throw new NoSuchNodeException(node, this.name);
            }
            return getNodes().get(node);
        }
    }

    public void addNode(Node node) {
        this.nodes.put(node.name, node);
    }

    public Node startNode() {
        return nodes.values().stream().filter(t -> t.getType().equals(Node.Type.START)).findFirst().get();
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Flow flow = (Flow)o;
        return Objects.equals(name, flow.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void onStateUpdate(FlowContext ctx) {
        plugins.forEach(plugin -> {
            try {
                plugin.onStateUpdate(ctx);
            } catch (Throwable e) {
                plugin.onPluginFlowException(e, ctx);
            }
        });
    }

    public void preAction(ActionRouterAdapter action, FlowContext ctx) {
        plugins.forEach(plugin -> {
            try {
                plugin.preAction(action, ctx);
            } catch (Throwable e) {
                plugin.onPluginFlowException(e, ctx);
            }
        });
    }

    public void postAction(ActionRouterAdapter action, FlowContext ctx) {
        plugins.forEach(plugin -> {
            try {
                plugin.postAction(action, ctx);
            } catch (Throwable e) {
                plugin.onPluginFlowException(e, ctx);
            }
        });
    }

    public void statusTransition(
        Action action,
        Pair<FlowStatus, String> sourceStatus, Pair<FlowStatus, String> targetStatus, FlowContext ctx) {
        plugins.forEach(plugin -> {
            try {
                plugin.onStatusTransition(action,sourceStatus, targetStatus, ctx);
            } catch (Throwable e) {
                plugin.onPluginFlowException(e, ctx);
            }
        });
    }

    public void onActionException(ActionRouterAdapter action, Throwable e, FlowContext ctx) {
        plugins.forEach(plugin -> {
            try {
                if (e == null) {
                    return;
                } else if (e instanceof PauseException) {
                    plugin.onPauseException(action, (PauseException)e, ctx);
                } else if (e instanceof InterruptException) {
                    plugin.onInterruptException(action, (InterruptException)e, ctx);
                } else if (e instanceof ParameterException) {
                    plugin.onArgumentException(action, (ParameterException)e, ctx);
                } else if (e instanceof RuntimeException) {
                    plugin.onRuntimeException(action, (RuntimeException)e, ctx);
                } else if (e instanceof Throwable) {
                    plugin.onThrowable(action, e, ctx);
                }
            } catch (Throwable t) {
                plugin.onPluginFlowException(t, ctx);
            }
        });
    }

 

    public void onContextCreateException(ContextCreateException e, FlowRequest<?> request) {
        plugins.forEach(plugin -> {
            try {
                plugin.onContextCreateException(e, request);
            } catch (Throwable t) {
                plugin.onPluginIoException(t, request);
            }
        });
    }

    public void onSessionException(SessionException e, FlowRequest<?> request) {
        plugins.forEach(plugin -> {
            try {
                plugin.onSessionException(e, request);
            } catch (Throwable t) {
                plugin.onPluginIoException(t, request);
            }
        });
    }

    public void onStatusException(StatusException e, FlowRequest<?> request) {
        plugins.forEach(plugin -> {
            try {
                plugin.onStatusException(e, request);
            } catch (Throwable t) {
                plugin.onPluginIoException(t, request);
            }
        });
    }

    public void onOtherIoException(Exception e, FlowRequest<?> request) {
        plugins.forEach(plugin -> {
            try {
                plugin.onOtherIoException(e, request);
            } catch (Throwable t) {
                plugin.onPluginIoException(t, request);
            }
        });
    }

    public void onNoSuchEventException(NoSuchEventException e, FlowContext ctx) {
        plugins.forEach(plugin -> {
            try {
                plugin.onNoSuchEventException(e, ctx);
            } catch (Throwable t) {
                plugin.onPluginFlowException(t, ctx);
            }
        });
    }

    public void onNoSuchNodeException(NoSuchNodeException e, FlowContext ctx) {
        plugins.forEach(plugin -> {
            try {
                plugin.onNoSuchNodeException(e, ctx);
            } catch (Throwable t) {
                plugin.onPluginFlowException(t, ctx);
            }
        });
    }
}
