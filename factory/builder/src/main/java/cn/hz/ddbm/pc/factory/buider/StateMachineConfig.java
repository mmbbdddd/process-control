package cn.hz.ddbm.pc.factory.buider;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.profile.PcService;
import cn.hz.ddbm.pc.profile.StablePcService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

public interface StateMachineConfig<S> extends InitializingBean {
    String machineId();

    String describe();

    Flow build(Container container) throws Exception;

    List<Plugin> plugins();

    SessionManager sessionManager();

    StatusManager statusManager();

    Map<String, Object> attrs();

    default Action getAction(String action) {
        return InfraUtils.getActionBean(action);
    }

    @Override
    default void afterPropertiesSet() throws Exception {
        InfraUtils.getContainer().getBean(PcService.class).addFlow(build(InfraUtils.getContainer()));
    }

}
