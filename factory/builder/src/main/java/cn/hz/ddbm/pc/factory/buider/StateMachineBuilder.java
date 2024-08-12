package cn.hz.ddbm.pc.factory.buider;

import cn.hz.ddbm.pc.core.ActionAttrs;
import cn.hz.ddbm.pc.core.Router;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import org.springframework.beans.factory.BeanFactory;

import java.util.Set;

public class StateMachineBuilder<S> {
    public static <S> Builder<S> builder() {
        return new Builder<>();
    }

    public static class Builder<S> {
        public StateMachine<S> build() {
            return null;
        }

        public ConfigurationConfigurer withConfiguration() {
            return null;
        }

        public States<S> withStates() {
            return null;
        }

        public Transitions<S> withTransitions() {
            return new Transitions<>();
        }

        public Routers withRouters() {
            return null;
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

    public static class Routers {


        public Routers register(String simpleRouter, Router expressionRouter) {
            return null;
        }
    }


}
