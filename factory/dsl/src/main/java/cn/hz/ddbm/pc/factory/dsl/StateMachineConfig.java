package cn.hz.ddbm.pc.factory.dsl;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.action.NoneAction;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.profile.PcService;

import javax.annotation.PostConstruct;
import java.util.List;

public interface StateMachineConfig<S> {
    String flowId();

    String describe();

    Flow build() throws Exception;

    List<Plugin> plugins();

    SessionManager.Type session();

    StatusManager.Type status();


    default Action getAction(String action) {
        return new NoneAction();
    }

    @PostConstruct
    default void afterPropertiesSet() throws Exception {
        Flow flow = build();
        flow.setPlugins(plugins());
        InfraUtils.getBean(PcService.class).addFlow(flow);
    }

}
