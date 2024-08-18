package cn.hz.ddbm.pc.factory.dsl;

import cn.hz.ddbm.pc.core.Fsm;
import cn.hz.ddbm.pc.core.Node;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import lombok.Getter;

import java.util.*;

public interface FSM<S extends Enum<S>> {
    String flowId();

    String describe();

    List<Plugin> plugins();

    SessionManager.Type session();

    StatusManager.Type status();

    Map<S, Node.Type> nodes();

    Map<String, Set<S>> maybeResults(Map<String, Set<S>> map);

    void transitions(Transitions<S> transitions);

    Map<S, Profile.StepAttrs> stateAttrs();

    Map<String, Profile.ActionAttrs> actionAttrs();

    Profile<S> profile();


    default Fsm<S> build() throws Exception {
        Map<S, Profile.StepAttrs> stepAttrsMap = stateAttrs();
        Profile<S>                profile      = profile();
        profile.setStatusManager(status());
        profile.setSessionManager(session());
        profile.setActions(actionAttrs());
        profile.setStates(stepAttrsMap);
        Map<String, Set<S>> maybeResults = new HashMap<String, Set<S>>();
        ;
        profile.setMaybeResults(maybeResults(maybeResults));
        Fsm<S> fsm = Fsm.of(flowId(), describe(), nodes(), profile);
        fsm.setPlugins(plugins());
        Transitions<S> transitions = new Transitions<>();
        transitions(transitions);
        transitions.transitions.forEach(t -> {
            if (t.getType().equals(Fsm.FsmRecordType.SAGA)) {
                fsm.getFsmTable().saga(t.getFrom(), t.event, t.failover, t.action,t.router);
            }
            if (t.getType().equals(Fsm.FsmRecordType.ROUTER)) {
                fsm.getFsmTable().router(t.getFrom(), t.getEvent(), t.getAction(),t.router);
            }
            if (t.getType().equals(Fsm.FsmRecordType.TO)) {
                fsm.getFsmTable().to(t.getFrom(), t.getEvent(), t.getAction(), t.to);
            }
        });
//        InfraUtils.getBean(PcService.class).addFlow(flow);
        return fsm;
    }


    class Transitions<S> {
        List<FSM.Transition<S>> transitions;

        public Transitions() {
            this.transitions = new ArrayList<>();
        }


        public Transitions<S> to(S from, String event, S to) {
            transitions.add(Transition.toOf(from, event, Coasts.NONE_ACTION, to));
            return this;
        }

        public Transitions<S> to(S from, String event, String action, S to) {
            transitions.add(Transition.toOf(from, event, action, to));
            return this;
        }

        public Transitions<S> router(S node, String event, String action, String router) {
            transitions.add(Transition.routerOf(node, event, action, router));
            return this;
        }

        public Transitions<S> saga(S node, String event, S failover, String action, String router) {
            transitions.add(Transition.sagaOf(node, event, failover, action, router));
            return this;
        }
    }

    @Getter
    class Transition<S> {
        Fsm.FsmRecordType type;
        S                 from;
        String            event;
        S                 failover;
        String            action;
        S                 to;
        String            router;


        public static <S> Transition<S> routerOf(S node, String event, String action, String router) {
            Transition<S> t = new Transition<S>();
            t.type   = Fsm.FsmRecordType.ROUTER;
            t.from   = node;
            t.event  = event;
            t.action = action;
            t.router = router;
            return t;
        }

        public static <S> Transition<S> toOf(S from, String event, String action, S to) {
            Transition<S> t = new Transition<S>();
            t.type   = Fsm.FsmRecordType.TO;
            t.from   = from;
            t.event  = event;
            t.action = action;
            t.to     = to;
            return t;
        }

        public static <S> Transition<S> sagaOf(S node, String event, S failover, String action, String router) {
            Transition<S> t = new Transition<S>();
            t.type     = Fsm.FsmRecordType.SAGA;
            t.from     = node;
            t.event    = event;
            t.action   = action;
            t.failover = failover;
            t.router   = router;
            return t;
        }
    }

}
