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
public class Fsm<S extends Enum<S>> {
    final String                           name;
    final String                           descr;
    final S                                init;
    final Profile                          profile;
    final Map<S, Node<S>>                  nodes;
    final Map<String, ExpressionRouter<S>> routers;
    @Setter
    List<Plugin> plugins;
    FsmTable<S> fsmTable;

    public Fsm(String name, String descr, Map<S, Node.Type> nodes, List<ExpressionRouter<S>> routers, Profile profile) {
        Assert.notNull(name, "flow.name is null");
        Assert.notNull(routers, "routers is null");
        Assert.notNull(nodes, "nodes is null");
        Assert.notNull(profile, "profile is null");
        this.name     = name;
        this.descr    = descr;
        this.profile  = profile;
        this.init     = nodes.entrySet().stream().filter(e -> e.getValue().equals(Node.Type.START)).findFirst().get().getKey();
        this.nodes    = nodes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Node<>(e.getValue(), e.getKey(), profile)));
        this.fsmTable = new FsmTable<>(this);
        this.routers  = routers.stream().collect(Collectors.toMap(ExpressionRouter::routerName, t -> t));
        this.plugins  = new ArrayList<>();
    }


    /**
     * 定义流程参数
     *
     * @param name
     * @param profile
     * @return
     */
    public static <S extends Enum<S>> Fsm<S> of(String name, String descr, Map<S, Node.Type> nodes, List<ExpressionRouter<S>> routers, Profile profile) {
        return new Fsm<>(name, descr, nodes, routers, profile);
    }

    /**
     * 定义开发环境流程参数
     *
     * @param name
     * @return
     */
    public static <S extends Enum<S>> Fsm<S> devOf(String name, String descr, Map<S, Node.Type> nodes, List<ExpressionRouter<S>> routers) {
        List<String> plugins = new ArrayList<>();
        plugins.add(Coasts.PLUGIN_DIGEST_LOG);
        plugins.add(Coasts.PLUGIN_ERROR_LOG);
        Fsm<S> flow = new Fsm<>(name, descr, nodes, routers, Profile.devOf());
        flow.plugins = InfraUtils.getByCodesOfType(plugins, Plugin.class);
        return flow;
    }


    public <T> void execute(FlowContext<S, ?> ctx) throws RouterException, ActionException {
        Assert.isTrue(true, "ctx is null");
        S            node = ctx.getStatus().getNode();
        FsmRecord<S> atom = fsmTable.find(node, ctx.getEvent());
        Assert.notNull(atom, String.format("找不到事件处理器%s@%s", ctx.getEvent().getCode(), ctx.getStatus().getNode()));

        ActionRouter<S> executor = new ActionRouter<>(atom.getType(),
                atom.getFrom(),
                atom.getEvent(),
                atom.getAction(),
                atom.getFailover(),
                atom.getRouter(),
                ctx.getFlow().getPlugins());
        ctx.setExecutor(executor);
        atom.init(ctx);
        atom.execute(ctx);
    }


    public S startStep() {
        return init;
    }


    public Set<String> nodeNames() {
        return nodes.keySet().stream().map(Enum::name).collect(Collectors.toSet());
    }

    public boolean isEnd(S state) {
        return nodes.get(state).equals(Node.Type.END);
    }

    public Node<S> getNode(S state) {
        return nodes.get(state);
    }

    public boolean isRouter(S node) {
        if (!nodes.containsKey(node)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "{" + "name:'" + name + '\'' + ", descr:'" + descr + '\'' + ", init:" + init + ",nodes:" + nodes + ", fsmTable:" + Arrays.toString(fsmTable.records.toArray(
                new FsmRecord[fsmTable.records.size()])) + '}';
    }


    public enum STAUS {
        RUNNABLE, PAUSE, CANCEL, FINISH
    }

    @Data
    public static class FsmTable<S extends Enum<S>> {
        private Set<FsmRecord<S>> records;
        private Fsm<S>            fsm;

        public FsmTable(Fsm<S> fsm) {
            this.records = new HashSet<>();
            this.fsm     = fsm;
        }

        public FsmRecord<S> find(S node, Event event) {
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
        public void to(S from, String e, String action, S to) {
            this.records.add(new FsmRecord<>(FsmRecordType.TO, from, Event.of(e), action, null, new ToRouter<>(from, to)));
        }

        public void router(S from, String e, String action, String routerName) {
            ExpressionRouter<S> router = fsm.routers.get(routerName);
            this.records.add(new FsmRecord<>(FsmRecordType.ROUTER, from, Event.of(e), action, null, router));
        }

        public void saga(S from, String e, S failover, String action, String routerName) {
            ExpressionRouter<S> router = fsm.routers.get(routerName);
            this.records.add(new FsmRecord<>(FsmRecordType.SAGA, from, Event.of(e), action, failover, router));
        }


        @Override
        public String toString() {
            return Arrays.toString(records.toArray());
        }
    }

    @Data
    public static class FsmRecord<S extends Enum<S>> {
        FsmRecordType   type;
        S               from;
        Event           event;
        String          action;
        S               failover;
        Router<S>       router;
        ActionRouter<S> actionRouter;

        public FsmRecord(FsmRecordType type, S from, Event event, String action, S failover, Router<S> router) {
            this.type     = type;
            this.from     = from;
            this.event    = event;
            this.action   = action;
            this.failover = failover;
            this.router   = router;
        }


        public void init(FlowContext<S, ?> ctx) {
            if (null == actionRouter) {
                synchronized (this) {
                    actionRouter = new ActionRouter<>(type, from, event, action, failover, router, ctx.getFlow().getPlugins());
                }
            }
        }

        public void execute(FlowContext<S, ?> ctx) throws RouterException, ActionException {
            actionRouter.execute(ctx);
        }
    }

    public enum FsmRecordType {
        TO, SAGA, ROUTER
    }
}
