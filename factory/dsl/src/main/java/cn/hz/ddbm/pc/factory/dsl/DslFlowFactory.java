package cn.hz.ddbm.pc.factory.dsl;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.factory.FlowFactory;
import cn.hz.ddbm.pc.factory.ResourceLoader;

import java.util.Collections;
import java.util.List;

/**
 * 检测类路径下所有的状态机（StateMachineConfig）
 */
public class DslFlowFactory implements FlowFactory {

    @Override
    public List<Flow> loadFlowByResource(ResourceLoader resource) {
        return Collections.emptyList();
    }
}
