package cn.hz.ddbm.pc.newcore.factory;

import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaFlow;

import java.util.List;

public interface SAGA {
    default SagaFlow build() {
        SagaFlow flow = SagaFlow.of(flowId(), pipeline());

        return flow;
    }

    String flowId();

    List<Class<? extends SagaAction>> pipeline();

    List<Plugin> plugins();


}
