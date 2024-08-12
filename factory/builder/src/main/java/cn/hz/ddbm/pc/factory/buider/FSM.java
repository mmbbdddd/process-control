package cn.hz.ddbm.pc.factory.buider;

import org.springframework.beans.factory.BeanFactory;

import java.util.List;

public interface FSM<S> {
    String machineid();

    StateMachine<S, String, String, String> build(BeanFactory beanFactory) throws Exception;

    List<String> plugins();

    String sessionManager();

    String statusManager();
}
