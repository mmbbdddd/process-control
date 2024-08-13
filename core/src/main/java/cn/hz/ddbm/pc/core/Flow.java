package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.*;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.router.ToRouter;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Flow {
    final String              name;
    final String              descr;
    final SessionManager      sessionManager;
    final StatusManager       statusManager;
    final Node                init;
    final Container           container;
    final Map<String, Object> attrs;
    final Map<String, Node>   ends;
    final Map<String, Node>   nodes;
    Map<String, Router<?>> routers;
    List<Plugin>           plugins;
    FsmTable               fsmTable;

    public Flow(String name, String descr, String init, Set<String> ends, Set<String> nodes, SessionManager sessionManager, StatusManager statusManager, Map<String, Object> attrs) {
        Assert.notNull(name, "flow.name is null");
        Assert.notNull(init, "start.node is null");
        Assert.notNull(ends, "ends.node is null");
        this.name           = name;
        this.descr          = descr;
        this.attrs          = attrs == null ? new HashMap<>() : attrs;
        this.init           = new Node(Node.Type.START, init, new HashMap<>());
        this.ends           = ends.stream().map(e -> new Node(Node.Type.END, e, new HashMap<>())).collect(Collectors.toMap(Node::getName, t -> t));
        this.nodes          = nodes.stream().map(e -> new Node(Node.Type.TASK, e, new HashMap<>())).collect(Collectors.toMap(Node::getName, t -> t));
        this.container      = InfraUtils.getContainer();
        this.sessionManager = sessionManager == null ? buildSessionManager() : sessionManager;
        this.statusManager  = statusManager == null ? buildStatusManager() : statusManager;
        this.fsmTable       = new FsmTable();
        this.routers        = new HashMap<>();
        this.plugins        = new ArrayList<>();
    }

    private StatusManager buildStatusManager() {
        String statusManager = this.getAttrs().get(Coasts.STATUS_MANAGER) == null ?
                Coasts.STATUS_MANAGER_REDIS :
                (String) this.getAttrs().get(Coasts.STATUS_MANAGER);
        return container.getBean(statusManager, StatusManager.class);
    }

    private SessionManager buildSessionManager() {
        String sessionManager = this.getAttrs().get(Coasts.SESSION_MANAGER) == null ?
                Coasts.SESSION_MANAGER_REDIS :
                (String) this.getAttrs().get(Coasts.SESSION_MANAGER);
        return container.getBean(sessionManager, SessionManager.class);
    }

    /**
     * 定义流程参数
     *
     * @param name
     * @param attrs
     * @return
     */
    public static Flow of(String name, String descr, String init, Set<String> ends, Set<String> nodes, SessionManager sessionManger, StatusManager statusManager, Map<String, Object> attrs) {
        Flow flow = new Flow(name, descr, init, ends, nodes, sessionManger, statusManager, attrs);
        return flow;
    }

    /**
     * 定义开发环境流程参数
     *
     * @param name
     * @return
     */
    public static Flow devOf(String name, String descr, String init, Set<String> ends, Set<String> nodes) {
        List<String> plugins = new ArrayList<>();
        plugins.add(Coasts.PLUGIN_DIGEST_LOG);
        plugins.add(Coasts.PLUGIN_ERROR_LOG);

        SessionManager sessionManager1 = InfraUtils.getSessionManager(Coasts.SESSION_MANAGER_MEMORY);
        StatusManager  statusManager1  = InfraUtils.getStatusManager(Coasts.STATUS_MANAGER_MEMORY);
        Flow           flow            = new Flow(name, descr, init, ends, nodes, sessionManager1, statusManager1, new HashMap<>());
        flow.plugins = InfraUtils.getContainer().getByCodesOfType(plugins, Plugin.class);
        return flow;
    }

    public void addNode(Node node) {
        this.nodes.put(node.name, node);
    }

    /**
     * 定义流程的router
     */
    public void addRouter(Router router) {
        this.routers.put(router.routerName(), router);
    }

    /**
     * 定义流程事件绑定关系
     *
     * @param source
     * @param event
     * @param action
     * @param router
     */
    public void onEventRouter(String source, Event event, String action, ExpressionRouter router) {
        this.fsmTable.records.add(FsmRecord.builder()
                .from(source)
                .event(event)
                .action(action)
                .router(router.routerName())
                .build());
        addRouter(router);

    }

    public void onEventTo(String source, String event, String action, String to) {
        onEventTo(source, Event.of(event), action, to);
    }

    public void onEventTo(String source, Event event, String action, String to) {
        Router toRouter = new ToRouter(source, event, to);
        this.fsmTable.records.add(FsmRecord.builder()
                .from(source)
                .event(event)
                .action(action)
                .router(toRouter.routerName())
                .build());
        addRouter(toRouter);
    }

    //初始化Flow的bean属性
    public void validate() {
        Assert.notNull(fsmTable, "fsm table is null");
    }


    public Node getNode(String stepName) {
        Node node = nodes.get(stepName);
        if (null == node) {
            node = ends.get(stepName);
        }
        return node;
    }


    public <T> void execute(FlowContext<?> ctx) throws StatusException, SessionException, RouterException, ActionException {
        Assert.isTrue(true, "ctx is null");
//        try {
        String node = ctx.getStatus()
                .getNode();
        FsmRecord atom = fsmTable.find(node, ctx.getEvent());
        Assert.notNull(atom, String.format("找不到事件处理器%s@%s", ctx.getEvent(), ctx.getFlow()
                .getFsmTable()
                .toString()));

        AtomExecutor atomExecutor = AtomExecutor.builder()
                .event(atom.getEvent())
                .plugins(plugins)
                .actionRouter(InfraUtils.getActionRouter(atom.getAction(), ctx.getFlow().routers.get(atom.getRouter())))
                .build();
        ctx.setAtomExecutor(atomExecutor);
        atomExecutor.execute(ctx);
//        } catch (IOException e) {
//            e.printStackTrace();
//            ctx.syncPayLoad();
//        } catch (IllegalArgumentException e) {
//            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
//            return;
//        } catch (InterruptedFlowException e) {
//            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
//            flush(ctx);
//            return;
//        } catch (PauseFlowException e) {
//            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
//            flush(ctx);
//            return;
//        } catch (Throwable e) {
//            Logs.error.error("{},{}", ctx.getFlow().name, ctx.getId(), e);
//            flush(ctx);
//            return;
//        }
//        if (fluent && isCanContinue(ctx)) {
//            ctx.setEvent(Event.of(Coasts.EVENT_DEFAULT));
//            execute(ctx);
//        }
    }


    /**
     * 刷新状态到基础设施
     */
    private void flush(FlowContext<?> ctx) throws SessionException, StatusException {
        sessionManager.flush(ctx);
        statusManager.flush(ctx);
    }


    public Node startStep() {
        return init;
    }


    public Set<String> nodeNames() {
        return new HashSet<String>() {{
            add(init.name);
            addAll(ends.keySet());
            addAll(nodes.keySet());
        }};
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

        public FsmRecord find(String node, Event event) {
            return records.stream()
                    .filter(r -> Objects.equals(r.getFrom(), node) && Objects.equals(r.getEvent(), event))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * 暴露给用户的接口
         * 内部被拆分成1+N
         * 1，node=>event==>nodeOf(action,router)
         * 2,nodeOf(action,router)==>routerResultEvent==>routerResultNode
         * 参见onInner
         */
        public void on(String node, String event, String action, String routerName) {

        }

        private void onInner(String node, Event event, ActionRouter actionRouter) {
            this.records.add(FsmRecord.builder().from(node).event(event).to(actionRouter.status()).build());
            actionRouter.eventToNodes().forEach((routerResultEvent, routerResultNode) -> {
                this.records.add(FsmRecord.builder().from(actionRouter.status()).event(routerResultEvent).to(routerResultNode).build());
            });
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
        Event  event;
        String to;
        String action;
        String router;

        @Override
        public String toString() {
            return "{" + "from:'" + from + '\'' + ", event:'" + event + '\'' + ", action:'" + action + '\'' + ", router:'" + router + '\'' + '}';
        }
    }


}
