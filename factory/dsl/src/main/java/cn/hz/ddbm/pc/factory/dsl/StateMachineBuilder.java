package cn.hz.ddbm.pc.factory.dsl;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.profile.PcService;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 写法风格同spring-state-machine。
 * 降低学习和迁移成本
 *
 * @param <S>
 */
public class StateMachineBuilder<S> {
    public static <S extends Enum<S>> Builder<S> builder(StateMachineConfig<S> fsm) {
        return new Builder<>(fsm);
    }

    public static class Builder<S extends Enum<S>> {
        StateMachineConfig<S> fsm;
        States<S>             states;
        Transitions<S>        transitions;
        Routers               routers;

        public Builder(StateMachineConfig<S> fsm) {
            this.fsm         = fsm;
            this.states      = new States<>();
            this.routers     = new Routers();
            this.transitions = new Transitions<>();
        }

        public Flow build() {
            String      init    = states.init.name();
            Set<String> ends    = states.ends.stream().map(Enum::name).collect(Collectors.toSet());
            Set<String> nodes   = states.states.stream().map(Enum::name).collect(Collectors.toSet());
            Profile     profile = InfraUtils.getBean(PcService.class).profile();
            this.routers.routers.forEach((rn, r) -> profile.addRouter(r));
            Flow flow = Flow.of(fsm.flowId(), fsm.describe(), init, ends, nodes, profile);
            this.transitions.transitions.forEach(transition -> {
                if (Objects.equals(null, transition.getTo())) {
                    flow.router(transition.getFrom().name(), transition.getEvent(), fsm.getAction(transition.getAction()), transition.getRouter());
                } else {
                    flow.to(transition.getFrom().name(), transition.getEvent(), fsm.getAction(transition.getAction()), transition.getTo().name());
                }
            });
            return flow;
        }


        public States<S> withStates() {
            return states;
        }

        public Transitions<S> withTransitions() {
            return transitions;
        }

        public Routers withRouters() {
            return routers;
        }
    }


    public static class States<S> {
        S      init;
        Set<S> ends;
        Set<S> states;

        public States<S> initial(S init) {
            this.init = init;
            return this;
        }

        public States<S> states(Set<S> states) {
            this.states = states;
            return this;
        }

        public States<S> ends(S... ends) {
            this.ends = Arrays.stream(ends).collect(Collectors.toSet());
            return this;
        }

    }

    public static class Transitions<S extends Enum> {
        List<Transition<S>> transitions;

        public Transitions() {
            this.transitions = new ArrayList<>();
        }

        public Transitions<S> to(S from, String event, String action, S to) {
            transitions.add(new Transition<>(from, event, action, to));
            return this;
        }

        public   Transitions<S> router(S node, String event, String action, String router) {
            transitions.add(new Transition<>(node, event, action, router));
            return this;
        }

    }

    public static class Routers {
        Map<String, ExpressionRouter> routers;

        public Routers() {
            this.routers = new HashMap<>();
        }

        public Routers add(ExpressionRouter router) {
            this.routers.put(router.routerName(), router);
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


}
