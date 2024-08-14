package cn.hz.ddbm.pc.factory.dsl;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.factory.Resource;

public class DslResource extends Resource {
    StateMachineConfig<?> config;

    public DslResource(StateMachineConfig<?> smc) {
        this.config = smc;
    }

    @Override
    public Flow resolve() {
        try {
            return config.build(InfraUtils.getContainer());
        } catch (Exception e) {
            //todo
            throw new RuntimeException(e);
        }
    }
}
