package cn.hz.ddbm.pc.factory.buider;

import cn.hz.ddbm.pc.core.ActionAttrs;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.Router;
import org.springframework.beans.factory.BeanFactory;

import java.util.Set;

/**
 * 写法风格同spring-state-machine。
 * 降低学习和迁移成本
 *
 * @param <S>
 */
public class StateMachineBuilder<S> {
    public static <S> Builder<S> builder(StateMachineConfig<S> fsm) {
        return new Builder<>(fsm);
    }

    public static class Builder<S> {
        StateMachineConfig<S>   fsm;
        ConfigurationConfigurer configurer;
        States<S>               states;
        Transitions<S>          transitions;
        Routers<S>              routers;

        public Builder(StateMachineConfig<S> fsm) {
            this.fsm         = fsm;
            this.configurer  = new ConfigurationConfigurer();
            this.states      = new States<>();
            this.transitions = new Transitions<>();
            this.routers     = new Routers<>();
        }

        public Flow build() {
            String      init  = null;
            Set<String> ends  = null;
            Set<String> nodes = null;
            Flow        flow  = Flow.of(fsm.machineId(), fsm.describe(), init, ends, nodes, fsm.sessionManager(), fsm.statusManager(), null);
//            flow.addNode();
//            flow.addRouter();
//            flow.getFsmTable();
//            flow.setFluent(true);
            return flow;
        }

        public ConfigurationConfigurer withConfiguration() {
            return configurer;
        }

        public States<S> withStates() {
            return states;
        }

        public Transitions<S> withTransitions() {
            return transitions;
        }

        public Routers<S> withRouters() {
            return routers;
        }
    }

    public static class ConfigurationConfigurer {

        public ConfigurationConfigurer machineId(String machineid) {
            return this;
        }

        public void beanFactory(BeanFactory beanFactory) {

        }
    }

    public static class States<S> {
        S      init;
        Set<S> ends;

        public States<S> initial(String unpaid) {
            return null;
        }

        public void states(Set<S> enumSet) {

        }

        public States<S> ends(S... ends) {
            return null;
        }
    }

    public static class Transitions<S> {

        public Transitions<S> to(S node, String event, String action, S to) {
            return null;
        }

        public <A extends ActionAttrs> Transitions<S> router(S node, String event, String action, String router, A attr) {
            return null;
        }

    }

    public static class Routers<S> {

        public Routers<S> register(String simpleRouter, Router expressionRouter) {
            return null;
        }
    }


}
