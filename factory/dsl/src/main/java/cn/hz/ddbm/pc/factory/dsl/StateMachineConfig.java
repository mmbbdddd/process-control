package cn.hz.ddbm.pc.factory.dsl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.action.NoneAction;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.profile.PcService;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public interface StateMachineConfig<S extends StateMachineConfig.State> {
    String flowId();

    String describe();

    Class<S> fsm();

//    Flow build() throws Exception;

    List<Plugin> plugins();

    SessionManager.Type session();

    StatusManager.Type status();

    List<ExpressionRouter> routers();

    void transitions(Transitions<S> transitions);

    Map<String, Profile.StepAttrs> stateAttrs();

    Map<String, Profile.ActionAttrs> actionAttrs();

    default Action getAction(String action) {
        return new NoneAction();
    }


    @PostConstruct
    default void afterPropertiesSet() throws Exception {
        Map<String, Profile.StepAttrs> stepAttrsMap = stateAttrs();
        Profile                        profile      = Profile.defaultOf();
        profile.setStatusManager(status());
        profile.setSessionManager(session());
        profile.setActions(actionAttrs());
        profile.setStates(stepAttrsMap);

        Class          genericsType = (Class) TypeUtil.getGenerics(this.getClass())[0].getActualTypeArguments()[0];
        Map<String, S> enums        = EnumUtil.getEnumMap(genericsType);
        Set<Node> nodes = enums.values().stream()
                               .map(it -> new Node(it.type(), it.name(), stepAttrsMap.get(it.name()),profile))
                               .collect(Collectors.toSet());
        Flow flow = Flow.of(flowId(), describe(), nodes, routers(), profile);
        flow.setPlugins(plugins());
        Transitions<S> transitions = new Transitions<>();
        transitions(transitions);
        transitions.transitions.forEach(t -> {
            if (t.getTo() != null) {
                flow.to(t.getFrom().name(), t.getEvent(), t.getAction(), t.getTo().name());
            } else {
                flow.router(t.getFrom().name(), t.getEvent(), t.getAction(), t.getRouter());
            }
        });
        InfraUtils.getBean(PcService.class).addFlow(flow);
    }

    class Transitions<S> {
        List<StateMachineConfig.Transition<S>> transitions;

        public Transitions() {
            this.transitions = new ArrayList<>();
        }

        public Transitions<S> to(S from, String event, String action, S to) {
            transitions.add(new StateMachineConfig.Transition<>(from, event, action, to));
            return this;
        }

        public Transitions<S> router(S node, String event, String action, String router) {
            transitions.add(new Transition<>(node, event, action, router));
            return this;
        }

    }

    @Getter
    static class Transition<S> {
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

    public interface State {
        String name();

        cn.hz.ddbm.pc.core.Node.Type type();
    }

}
