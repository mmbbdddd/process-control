package cn.hz.ddbm.pc.factory;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.factory.buider.StateMachineBuilder;

import java.util.Collections;
import java.util.List;

public class DslFlowFactory implements FlowFactory<DslFlowFactory.DslResouce> {

    @Override
    public List<Flow> loadBeanDefinitions(ResourceLoader<DslResouce> resource) {
        return Collections.emptyList();
    }

    static class DslResouce extends Resource {
        StateMachineBuilder<?> stateMachineBuilder;
    }
}
