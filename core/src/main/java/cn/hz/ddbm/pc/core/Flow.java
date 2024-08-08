package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.exception.InterruptedFlowException;
import cn.hz.ddbm.pc.core.exception.PauseFlowException;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hz.ddbm.pc.core.utils.InfraUtils.releaseLock;

@Getter
@Slf4j
public class Flow {
    String              domain;
    String              name;
    Boolean             fluent = true;
    String              sessionManager;
    String              statusManager;
    List<String>        plugins;
    FsmTable            fsmTable;
    Map<String, Object> attrs;

    Map<String, Node> nodes;

    public Flow(String name, List<Node> nodes, List<String> plugins, List<String> routers, Map<String, Object> attrs) {
        Assert.notNull(name, "flow.name is null");
        Assert.notNull(nodes, "flow.nodes is null");
        plugins      = null == plugins ? Collections.emptyList() : plugins;
        routers      = null == routers ? Collections.emptyList() : routers;
        this.name    = name;
        this.attrs   = attrs;
        this.plugins = plugins;
        this.nodes   = nodes.stream().collect(Collectors.toMap(Node::getName, t -> t));
    }

    //初始化Flow的bean属性
    public void validate() {
        Assert.isTrue(nodes.values().stream().filter(n -> n.type.equals(Node.Type.START)).count() == 1, "node.start count != 1");
        Assert.isTrue(nodes.values().stream().anyMatch(n -> n.type.equals(Node.Type.END)), "node.end count != 1");
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
        try {
            if (Boolean.FALSE.equals(tryLock(ctx))) {
                return;
            }
            Assert.isTrue(ctx != null, "ctx is null");
            String       node         = getOrInitNode(ctx);
            FsmRecord    atom         = fsmTable.onEvent(node, ctx.getEvent());
            AtomExecutor atomExecutor = AtomExecutor.builder().event(atom.getEvent()).plugins(InfraUtils.getPluginBeans(plugins)).action(InfraUtils.getActionBean(atom.getAction())).router(InfraUtils.getRouterBean(atom.getRouter())).build();
            ctx.setAtomExecutor(atomExecutor);
            atomExecutor.execute(ctx);
        } catch (IOException e) {
            ctx.flush();
        } catch (InterruptedFlowException e) {
            flush(ctx);
            releaseLock(ctx);
            return;
        } catch (PauseFlowException e) {
            flush(ctx);
            releaseLock(ctx);
            return;
        } catch (Throwable e) {
            flush(ctx);
            releaseLock(ctx);
            return;
        }
        if (fluent && isCanContinue(ctx)) {
            execute(ctx);
        }
    }

    private void releaseLock(FlowContext<?> ctx) {
        InfraUtils.releaseLock(String.format("%s:%s:%s", domain, ctx.getFlow().name, ctx.getId()));
    }

    private Boolean tryLock(FlowContext<?> ctx) {
        return InfraUtils.tryLock(String.format("%s:%s:%s", domain, ctx.getFlow().name, ctx.getId()), 10);
    }

    /**
     * 刷新状态到基础设施
     */
    private void flush(FlowContext<?> ctx) {

    }

    /**
     * 可继续运行
     * 1，流程状态是Runable状态
     * 2，节点状态类型是非end的
     * 3，运行时限制为false（执行次数限制等）
     *
     * @param ctx
     * @return
     */
    private boolean isCanContinue(FlowContext<?> ctx) {
        Flow.STAUS flowStatus = null;
        String     node       = null;
        Node       nodeObj    = null;
        Node.Type  nodeType   = null;
        if (!flowStatus.equals(STAUS.RUNNABLE)) {
            log.info("流程不可运行：{},{},{}", name, ctx.getId(), flowStatus.name());
            return false;
        }
        if (nodeType.equals(Node.Type.END)) {
            log.info("流程已结束：{},{},{}", name, ctx.getId(), node);
            return false;
        }
        Integer exeRetry  = InfraUtils.getNodeMetricsWindows(node, new Date()).getRetrys();
        Integer nodeRetry = nodeObj.getRetry();
        if (exeRetry > nodeObj.getRetry()) {
            log.info("流程已限流：{},{},{},{}>{}", name, ctx.getId(), node, exeRetry, nodeRetry);
            return false;
        }
        return true;
    }


    private String getOrInitNode(FlowContext<?> ctx) {
        return null;
    }

    public Node startStep() {
        return nodes.values().stream().filter(t -> t.type.equals(Node.Type.START)).findFirst().get();
    }


    public Set<String> nodeNames() {
        return nodes.keySet();
    }


    public enum STAUS {
        RUNNABLE, PAUSE, CANCEL, FINISH
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
