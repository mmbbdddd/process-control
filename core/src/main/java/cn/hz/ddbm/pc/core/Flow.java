package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.ActionException;
import cn.hz.ddbm.pc.core.exception.RouterException;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.router.ToRouter;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Data;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Flow {
    final String            name;
    final String            descr;
    final SessionManager    sessionManager;
    final StatusManager     statusManager;
    final Node              init;
    final Container         container;
    final Profile           profile;
    final Map<String, Node> ends;
    final Map<String, Node> nodes;
    Map<String, ExpressionRouter> routers;
    List<Plugin>                  plugins;
    FsmTable                      fsmTable;

    public Flow(String name, String descr, String init, Set<String> ends, Set<String> nodes, SessionManager sessionManager, StatusManager statusManager, Profile profile) {
        Assert.notNull(name, "flow.name is null");
        Assert.notNull(init, "start.node is null");
        Assert.notNull(ends, "ends.node is null");
        this.name           = name;
        this.descr          = descr;
        this.profile        = profile == null ? Profile.defaultOf() : profile;
        this.init           = new Node(init, this.profile.getStepAttrsOrDefault(init));
        this.ends           = ends.stream().map(e -> new Node(e, this.profile.getStepAttrsOrDefault(e))).collect(Collectors.toMap(Node::getName, t -> t));
        this.nodes          = nodes.stream().map(e -> new Node(e, this.profile.getStepAttrsOrDefault(e))).collect(Collectors.toMap(Node::getName, t -> t));
        this.container      = InfraUtils.getContainer();
        this.sessionManager = sessionManager == null ? buildSessionManager() : sessionManager;
        this.statusManager  = statusManager == null ? buildStatusManager() : statusManager;
        this.fsmTable       = new FsmTable();
        this.routers        = new HashMap<>();
        this.plugins        = new ArrayList<>();
    }

    private StatusManager buildStatusManager() {
        String statusManager = this.profile.getStatusManager() == null ?
                Coasts.STATUS_MANAGER_REDIS :
                this.profile.getStatusManager();
        return container.getBean(statusManager, StatusManager.class);
    }

    private SessionManager buildSessionManager() {
        String sessionManager = this.profile.getSessionManager() == null ?
                Coasts.SESSION_MANAGER_REDIS :
                this.profile.getSessionManager();
        return container.getBean(sessionManager, SessionManager.class);
    }

    /**
     * 定义流程参数
     *
     * @param name
     * @param profile
     * @return
     */
    public static Flow of(String name, String descr, String init, Set<String> ends, Set<String> nodes, SessionManager sessionManger, StatusManager statusManager, Profile profile) {
        Flow flow = new Flow(name, descr, init, ends, nodes, sessionManger, statusManager, profile);
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
        Flow           flow            = new Flow(name, descr, init, ends, nodes, sessionManager1, statusManager1, Profile.defaultOf());
        flow.plugins = InfraUtils.getContainer().getByCodesOfType(plugins, Plugin.class);
        return flow;
    }


    /**
     * 定义流程的router
     */
    public void addRouter(ExpressionRouter router) {
        this.routers.put(router.routerName(), router);
    }

    /**
     * 定义流程事件绑定关系
     * 1,增加路由关系
     * 2，增加节点（瞬态）
     *
     * @param from
     * @param event
     * @param action
     * @param router
     */
    public void router(String from, String event, Action action, ExpressionRouter router) {
        Event e = Event.of(event);
        this.fsmTable.on(from, e, action, router);
    }

    public void router(String from, String event, Action action, String routerName) {
        ExpressionRouter router = routers.get(routerName);
        router(from, event, action, router);
    }


    public void to(String source, String event, Action action, String to) {
        Event  e        = Event.of(event);
        Router toRouter = new ToRouter(source, to);
        this.fsmTable.on(source, e, action, toRouter);
//        addRouter(toRouter);
    }


    public Node getNode(String stepName) {
        Node node = nodes.get(stepName);
        if (null == node) {
            node = ends.get(stepName);
        }
        return node;
    }


    public <T> void execute(FlowContext<?> ctx) throws RouterException, ActionException {
        Assert.isTrue(true, "ctx is null");

        String node = ctx.getStatus()
                .getNode();
        FsmRecord atom = fsmTable.find(node, ctx.getEvent());
        Assert.notNull(atom, String.format("找不到事件处理器%s@%s", ctx.getEvent().getCode(), ctx.getStatus().getNode()));

        AtomExecutor atomExecutor = AtomExecutor.builder()
                .event(atom.getEvent())
                .plugins(plugins)
                .actionRouter(atom.getActionRouter())
                .build();
        ctx.setAtomExecutor(atomExecutor);

        atomExecutor.execute(ctx);

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

    public boolean isEnd(String state) {
        return ends.keySet().contains(state);
    }

    public State getStep(String state) {
        State sts = nodes.get(state);
        if (null == sts) {
            sts = routers.get(state);
        }
        return sts;
    }


    public enum STAUS {
        RUNNABLE, PAUSE, CANCEL, FINISH
    }


    @Data
    static class FsmTable {
        private Set<FsmRecord> records;

        public FsmTable() {
            this.records = new HashSet<>();
        }

        public FsmRecord find(String node, Event event) {
            return records.stream()
                    .filter(r -> Objects.equals(r.getFrom(), node) && Objects.equals(r.getEvent().getCode(), event.getCode()))
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
        public void on(String from, Event event, Action action, Router router) {
            onInner(from, event, new ActionRouter(from, action, router));
        }

        private void onInner(String from, Event event, ActionRouter actionRouter) {
            //增加外部event事件
            this.records.add(new FsmRecord(from, event, actionRouter));
            if (actionRouter.getRouter() instanceof ExpressionRouter) {
                String routerStatus = actionRouter.status();
                this.records.add(new FsmRecord(routerStatus, event, new ActionRouter(routerStatus, Coasts.NONE_ACTION, actionRouter.getRouter())));
            }
//            //增加瞬态事件
//            actionRouter.eventToNodes().forEach((routerResultEvent, routerResultNode) -> {
//                ToRouter toRouter = new ToRouter(actionRouter.status(), routerResultNode);
//                this.records.add(new FsmRecord(actionRouter.status(), routerResultEvent, new ActionRouter(from, Coasts.NONE_ACTION, toRouter)));
//            });
        }

        @Override
        public String toString() {
            return Arrays.toString(records.toArray());
        }
    }

    @Data
    static class FsmRecord {
        String       from;
        Event        event;
        ActionRouter actionRouter;
        String       to;

        public FsmRecord(String from, Event event, ActionRouter actionRouter) {
            this.from         = from;
            this.event        = event;
            this.actionRouter = actionRouter;
            this.to           = actionRouter.status();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FsmRecord fsmRecord = (FsmRecord) o;
            return Objects.equals(from, fsmRecord.from) && Objects.equals(event, fsmRecord.event);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, event);
        }

        @Override
        public String toString() {
            return "{" + "from:'" + from + '\'' + ", event:'" + event.getCode() + '\'' + ", action:'" + actionRouter.getAction()
                    .beanName() + '\'' + ", to:'" + to + '\'' + '}';
        }

    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", descr:'" + descr + '\'' +
                ", init:" + init +
                ", ends:" + ends +
                ", nodes:" + nodes +
                ", routers:" + Arrays.toString(routers.values().toArray(new Router[routers.size()])) +
                ", fsmTable:" + Arrays.toString(fsmTable.records.toArray(new FsmRecord[fsmTable.records.size()])) +
                '}';
    }
}
