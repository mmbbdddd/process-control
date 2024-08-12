package cn.hz.ddbm.pc.factory.buider;

import cn.hz.ddbm.pc.core.ActionAttrs;
import cn.hz.ddbm.pc.core.Router;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import org.springframework.beans.factory.BeanFactory;

import java.util.Set;

public class StateMachineBuilder {
    public static <F, E, A, R> Builder<F, E, A, R> builder() {
        return new Builder<>();
    }

    public static class Builder<F, E, A, R> {
        public StateMachine<F, E, A, R> build() {
            return null;
        }

        public ConfigurationConfigurer withConfiguration() {
            return null;
        }

        public States withStates() {
            return null;
        }

        public Transitions withTransitions() {
            return null;
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

    public static class States {

        public States initial(String unpaid) {
            return null;
        }

        public <E> void states(Set<E> enumSet) {

        }

        public States ends(String... ends) {
            return null;
        }
    }

    public static class Transitions {

        public Transitions to(String node, String event, String action, String to) {
            return null;
        }

        public <A extends ActionAttrs> Transitions router(String node, String event, String action, String router, A attr) {
            return null;
        }

    }
    public static class Routers {


        public Routers register(String simpleRouter, Router expressionRouter) {
            return null;
        }
    }


}
