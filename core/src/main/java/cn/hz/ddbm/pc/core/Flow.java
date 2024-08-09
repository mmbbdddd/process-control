package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.InterruptedFlowException;
import cn.hz.ddbm.pc.core.exception.PauseFlowException;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.router.ExpressionAnyRouter;
import cn.hz.ddbm.pc.core.router.ToRouter;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Flow {
    String name;
    String descr;
    @Setter
    Boolean fluent = true;
    String              sessionManager;
    String              statusManager;
    List<String>        plugins;
    FsmTable            fsmTable;
    Map<String, Object> attrs;

    Map<String, Node>   nodes;
    Map<String, Router> routers;

    public Flow(String name, String descr, List<String> plugins, Map<String, Object> attrs) {
        Assert.notNull(name, "flow.name is null");
        this.name           = name;
        this.descr          = descr;
        this.plugins        = null == plugins ? Collections.emptyList() : plugins;
        this.nodes          = new HashMap<>();
        this.routers        = new HashMap<>();
        this.fsmTable       = new FsmTable();
        this.attrs          = (null == attrs) ? new HashMap<>() : attrs;
        this.sessionManager = (this.getAttrs()
                .get(Coasts.SESSION_MANAGER) == null) ? Coasts.SESSION_MANAGER_REDIS : (String) this.getAttrs()
                .get(Coasts.SESSION_MANAGER);
        this.statusManager  = (this.getAttrs()
                .get(Coasts.STATUS_MANAGER) == null) ? Coasts.STATUS_MANAGER_REDIS : (String) this.getAttrs()
                .get(Coasts.STATUS_MANAGER);
    }

    /**
     * 定义流程参数
     *
     * @param name
     * @param plugins
     * @param attrs
     * @return
     */
    public static Flow of(String name, String descr, List<String> plugins, Map<String, Object> attrs) {
        return new Flow(name, descr, plugins, attrs);
    }

    /**
     * 定义开发环境流程参数
     *
     * @param name
     * @return
     */
    public static Flow devOf(String name, String descr) {
        List<String> plugins = new ArrayList<>();
        plugins.add(Coasts.PLUGIN_DIGEST_LOG);
        plugins.add(Coasts.PLUGIN_ERROR_LOG);
        HashMap<String, Object> attrs = new HashMap<>();
        attrs.put(Coasts.SESSION_MANAGER, Coasts.SESSION_MANAGER_MEMORY);
        attrs.put(Coasts.STATUS_MANAGER, Coasts.STATUS_MANAGER_MEMORY);
        return new Flow(name, descr, plugins, attrs);
    }

    public void addNode(Node node) {

    }

    /**
     * 定义流程的router
     */
    public void addRouter(Router router) {
        this.routers.put(router.name(), router);
    }

    /**
     * 定义流程事件绑定关系
     *
     * @param source
     * @param event
     * @param action
     * @param router
     */
    public void onEventRouter(String source, String event, String action, ExpressionAnyRouter router) {
        this.fsmTable.records.add(FsmRecord.builder()
                .from(source)
                .event(event)
                .action(action)
                .router(router.name())
                .build());
        addRouter(router);

    }

    public void onEventTo(String source, String event, String action, String to) {
        Router toRouter = new ToRouter(source, event, to);
        this.fsmTable.records.add(FsmRecord.builder()
                .from(source)
                .event(event)
                .action(action)
                .router(toRouter.name())
                .build());
        addRouter(toRouter);
    }

    //初始化Flow的bean属性
    public void validate() {
        Assert.notNull(fsmTable, "fsm table is null");
        Assert.isTrue(nodes.values()
                .stream()
                .filter(n -> n.type.equals(Node.Type.START))
                .count() == 1, "node.start count != 1");
        Assert.isTrue(nodes.values()
                .stream()
                .anyMatch(n -> n.type.equals(Node.Type.END)), "node.end count != 1");
    }


    public Node getNode(String stepName) {
        return nodes.get(stepName);
    }

    /**
     * 执行状态机
     *
     * @param ctx
     */
    List<Plugin> pluginBeans;

    public List<Plugin> getPluginBeans() {
        if (null != pluginBeans) {
            return pluginBeans;
        } else {
            synchronized (this) {
                Map<String, Plugin> map = InfraUtils.getPluginBeans();
                this.pluginBeans = plugins.stream()
                        .filter(Objects::nonNull)
                        .map(t -> map.getOrDefault(t, null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
            return pluginBeans;
        }
    }

    public <T> void execute(FlowContext<?> ctx) {
        Assert.isTrue(true, "ctx is null");
        if (Boolean.FALSE.equals(tryLock(ctx))) {
            return;
        }
        try {
            String node = ctx.getStatus()
                    .getNode();
            FsmRecord atom = fsmTable.onEvent(node, ctx.getEvent());
            Assert.notNull(atom, String.format("找不到事件处理器%s@%s", ctx.getEvent(), ctx.getFlow()
                    .getFsmTable()
                    .toString()));

            AtomExecutor atomExecutor = AtomExecutor.builder()
                    .event(atom.getEvent())
                    .plugins(getPluginBeans())
                    .action(InfraUtils.getActionBean(atom.getAction()))
                    .router(getRouters().get(atom.getRouter()))
                    .build();
            ctx.setAtomExecutor(atomExecutor);
            atomExecutor.execute(ctx);
        } catch (IllegalArgumentException e) {
            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
            ctx.flush();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            ctx.flush();
        } catch (InterruptedFlowException e) {
            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
            flush(ctx);
            releaseLock(ctx);
            return;
        } catch (PauseFlowException e) {
            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
            flush(ctx);
            releaseLock(ctx);
            return;
        } catch (Throwable e) {
            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
            flush(ctx);
            releaseLock(ctx);
            return;
        }
        if (fluent && isCanContinue(ctx)) {
            ctx.setEvent(Coasts.EVENT_DEFAULT);
            execute(ctx);
        }
    }

    private void releaseLock(FlowContext<?> ctx) {
        InfraUtils.releaseLock(String.format("%s:%s:%s", InfraUtils.getDomain(), ctx.getFlow().name, ctx.getId()));
    }

    private Boolean tryLock(FlowContext<?> ctx) {
        return InfraUtils.tryLock(String.format("%s:%s:%s", InfraUtils.getDomain(), ctx.getFlow().name, ctx.getId()), 10);
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
        Flow.STAUS flowStatus = ctx.getStatus()
                .getFlow();
        String node = ctx.getStatus()
                .getNode();
        Node      nodeObj  = getNode(node);
        Node.Type nodeType = nodeObj.getType();

        if (!flowStatus.equals(STAUS.RUNNABLE)) {
            Logs.flow.info("流程不可运行：{},{},{}", name, ctx.getId(), flowStatus.name());
            return false;
        }
        if (nodeType.equals(Node.Type.END)) {
            Logs.flow.info("流程已结束：{},{},{}", name, ctx.getId(), node);
            return false;
        }
        String windows = String.format("%s:%s:%s:%s", ctx.getFlow()
                .getName(), ctx.getId(), node, Coasts.NODE_RETRY);
        Integer exeRetry = InfraUtils.getMetricsTemplate()
                .get(windows);
        Integer nodeRetry = nodeObj.getRetry();
        if (exeRetry > nodeObj.getRetry()) {
            Logs.flow.info("流程已限流：{},{},{},{}>{}", name, ctx.getId(), node, exeRetry, nodeRetry);
            return false;
        }
        return true;
    }


    public Node startStep() {
        return nodes.values()
                .stream()
                .filter(t -> t.type.equals(Node.Type.START))
                .findFirst()
                .get();
    }


    public Set<String> nodeNames() {
        return nodes.keySet();
    }


    public enum STAUS {
        RUNNABLE, PAUSE, CANCEL, FINISH
    }


    @Data
    public static class FsmTable {
        private List<FsmRecord> records;

        public FsmTable() {
            this.records = new ArrayList<>();
        }

        public FsmRecord onEvent(String step, String event) {
            return records.stream()
                    .filter(r -> Objects.equals(r.getFrom(), step) && Objects.equals(r.getEvent(), event))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return Arrays.toString(records.toArray());
        }
    }

    @Data
    @Builder
    public static class FsmRecord {
        String from;
        String event;
        String action;
        String router;

        @Override
        public String toString() {
            return "{" + "from:'" + from + '\'' + ", event:'" + event + '\'' + ", action:'" + action + '\'' + ", router:'" + router + '\'' + '}';
        }
    }


}
