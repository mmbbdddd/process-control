package cn.hz.ddbm.pc.factory.dsl;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.factory.FlowFactory;
import cn.hz.ddbm.pc.factory.Resource;
import cn.hz.ddbm.pc.factory.ResourceLoader;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 检测类路径下所有的状态机（StateMachineConfig）
 */
public class DslFlowFactory extends FlowFactory<DslResource> {
    DslResourceLoader resourceLoader;
    @Override
    public ResourceLoader<DslResource> resourceLoader() {
        return resourceLoader;
    }

}
