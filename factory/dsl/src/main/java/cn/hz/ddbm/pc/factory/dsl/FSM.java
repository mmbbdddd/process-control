package cn.hz.ddbm.pc.factory.dsl;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.profile.PcService;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface FSM<S extends FSM.State> {
    String flowId();

    String describe();

    List<Plugin> plugins();

    SessionManager.Type session();

    StatusManager.Type status();

    List<ExpressionRouter> routers();

    void transitions(Transitions<S> transitions);

    Map<String, Profile.StepAttrs> stateAttrs();

    Map<String, Profile.ActionAttrs> actionAttrs();

    Profile profile();


    default Fsm build() throws Exception {
        Map<String, Profile.StepAttrs> stepAttrsMap = stateAttrs();
        Profile                        profile      = profile();
        profile.setStatusManager(status());
        profile.setSessionManager(session());
        profile.setActions(actionAttrs());
        profile.setStates(stepAttrsMap);

        Class          genericsType = (Class) TypeUtil.getGenerics(this.getClass())[0].getActualTypeArguments()[0];
        Map<String, S> enums        = EnumUtil.getEnumMap(genericsType);
        Set<Node>      nodes        = enums.values().stream().map(it -> new Node(it.type(), it.name(), profile)).collect(Collectors.toSet());
        Fsm            fsm          = Fsm.of(flowId(), describe(), nodes, routers(), profile);
        fsm.setPlugins(plugins());
        Transitions<S> transitions = new Transitions<>();
        transitions(transitions);
        transitions.transitions.forEach(t -> {
            if (t.getTo() != null) {
                fsm.to(t.getFrom().name(), t.getEvent(), t.getAction(), t.getTo().name());
            } else {
                fsm.router(t.getFrom().name(), t.getEvent(), t.getAction(), t.getRouter());
            }
        });
//        InfraUtils.getBean(PcService.class).addFlow(flow);
        return fsm;
    }

    interface State {
        String name();

        cn.hz.ddbm.pc.core.Node.Type type();
    }

    class Transitions<S> {
        List<FSM.Transition<S>> transitions;

        public Transitions() {
            this.transitions = new ArrayList<>();
        }

        public Transitions<S> to(S from, String event, S to) {
            to(from, event, Coasts.NONE, to);
            return this;
        }

        public Transitions<S> to(S from, String event, String action, S to) {
            transitions.add(new FSM.Transition<>(from, event, action, to));
            return this;
        }

        public Transitions<S> router(S node, String event, String action, String router) {
            transitions.add(new Transition<>(node, event, action, router));
            return this;
        }

    }

    @Getter
    class Transition<S> {
        S      from;
        String event;
        String action;
        S      to;
        String router;

        public Transition(S from, String event, String action, S to) {
            this.from   = from;
            this.event  = event;
            this.action = action;
            this.to     = to;
        }

        public Transition(S from, String event, String action, String router) {
            this.from   = from;
            this.event  = event;
            this.action = action;
            this.router = router;
        }
    }

}
