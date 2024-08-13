package cn.hz.ddbm.pc.factory.buider;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import org.springframework.beans.factory.BeanFactory;

import java.util.List;
import java.util.Map;

public interface StateMachineConfig<S> {
    String machineId();

    String describe();

    Flow build(BeanFactory beanFactory) throws Exception;

    List<Plugin> plugins();

    SessionManager sessionManager();

    StatusManager statusManager();

    Map<String, Object> attrs();
}
