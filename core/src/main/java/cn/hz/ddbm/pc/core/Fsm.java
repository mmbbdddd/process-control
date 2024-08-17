package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.ActionException;
import cn.hz.ddbm.pc.core.exception.RouterException;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.router.ToRouter;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Fsm {
    final String                        name;
    final String                        descr;
    final Node                          init;
    final Profile                       profile;
    final Map<String, Node>             nodes;
    final Map<String, ExpressionRouter> routers;
    @Setter
    List<Plugin> plugins;
    FsmTable fsmTable;

    public Fsm(String name, String descr, Set<Node> nodes, List<ExpressionRouter> routers, Profile profile) {
        Assert.notNull(name, "flow.name is null");
        Assert.notNull(routers, "routers is null");
        Assert.notNull(nodes, "nodes is null");
        Assert.notNull(profile, "profile is null");
        this.name     = name;
        this.descr    = descr;
        this.profile  = profile;
        this.init     = nodes.stream().filter(n -> n.getType().equals(Node.Type.START)).findFirst().get();
        this.nodes    = nodes.stream().collect(Collectors.toMap(Node::getName, t -> t));
        this.fsmTable = new FsmTable();
        this.routers  = routers.stream().collect(Collectors.toMap(t -> t.routerName(), t -> t));
        this.plugins  = new ArrayList<>();
    }


    /**
     * 定义流程参数
     *
     * @param name
     * @param profile
     * @return
     */
    public static Fsm of(String name, String descr, Set<Node> nodes, List<ExpressionRouter> routers, Profile profile) {
        return new Fsm(name, descr, nodes, routers, profile);
    }

    /**
     * 定义开发环境流程参数
     *
     * @param name
     * @return
     */
    public static Fsm devOf(String name, String descr, Set<Node> nodes, List<ExpressionRouter> routers) {
        List<String> plugins = new ArrayList<>();
        plugins.add(Coasts.PLUGIN_DIGEST_LOG);
        plugins.add(Coasts.PLUGIN_ERROR_LOG);
        Fsm flow = new Fsm(name, descr, nodes, routers, Profile.devOf());
        flow.plugins = InfraUtils.getByCodesOfType(plugins, Plugin.class);
        return flow;
    }


    /**
     * 定义流程事件绑定关系
     * 1,增加路由关系
     * 2，增加节点（瞬态）
     *
     * @param from
     * @param event
     * @param action
     * @param routerName
     */
    public void router(String from, String event, String action, String routerName) {
        ExpressionRouter router = routers.get(routerName);
        Event            e      = Event.of(event);
        this.fsmTable.on(from, e, action, router);
    }


    public void to(String source, String event, String action, String to) {
        Event  e        = Event.of(event);
        Router toRouter = new ToRouter(source, to);
        this.fsmTable.on(source, e, action, toRouter);
    }


    public Node getNode(String stepName) {
        return nodes.get(stepName);
    }


    public <T> void execute(FlowContext<?> ctx) throws RouterException, ActionException {
        Assert.isTrue(true, "ctx is null");

        String    node = ctx.getStatus().getNode();
        FsmRecord atom = fsmTable.find(node, ctx.getEvent());
        Assert.notNull(atom, String.format("找不到事件处理器%s@%s", ctx.getEvent().getCode(), ctx.getStatus().getNode()));

        AtomExecutor atomExecutor = AtomExecutor.builder().event(atom.getEvent()).plugins(plugins).actionRouter(atom.getState()).build();
        ctx.setAtomExecutor(atomExecutor);

        atomExecutor.execute(ctx);

    }


    public Node startStep() {
        return init;
    }


    public Set<String> nodeNames() {
        return new HashSet<String>() {{
            add(init.getName());
            addAll(nodes.keySet());
        }};
    }

    public boolean isEnd(String state) {
        return nodes.get(state).getType().equals(Node.Type.END);
    }

    public State getStep(String state) {
        State sts = nodes.get(state);
        if (null == sts) {
            sts = routers.get(state);
        }
        return sts;
    }

    public boolean isRouter(String node) {
        if (!nodes.containsKey(node)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "{" + "name:'" + name + '\'' + ", descr:'" + descr + '\'' + ", init:" + init + ",nodes:" + nodes + ", fsmTable:" + Arrays.toString(fsmTable.records.toArray(new FsmRecord[fsmTable.records.size()])) + '}';
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
            return records
                    .stream()
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
        public void on(String from, Event event, String action, Router router) {
            onInner(from, event, new ActionRouter(action, router));
        }

        private void onInner(String from, Event event, ActionRouter actionRouter) {
            //增加外部event事件
            this.records.addAll(actionRouter.fsmRecords(from, event));
        }

        @Override
        public String toString() {
            return Arrays.toString(records.toArray());
        }
    }

    @Data
    public static class FsmRecord {
        String       from;
        Event        event;
        ActionRouter state;
        String       to;

        public FsmRecord(String from, Event event, ActionRouter state) {
            this.from  = from;
            this.event = event;
            this.state = state;
            this.to    = state.status();
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
            return "{" + "from:'" + from + '\'' + ", event:'" + event.getCode() + '\'' + ", action:'" + state
                    .getAction() + '\'' + ", to:'" + to + '\'' + '}';
        }

    }
}
