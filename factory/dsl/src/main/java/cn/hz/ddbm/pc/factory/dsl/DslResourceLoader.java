package cn.hz.ddbm.pc.factory.dsl;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.factory.ResourceLoader;

import java.util.List;
import java.util.stream.Collectors;

public class DslResourceLoader implements ResourceLoader<DslResource> {
    @Override
    public List<DslResource> loadResources(Container container) {
        return container.getBeansOfType(StateMachineConfig.class).values().stream().map(DslResource::new).collect(Collectors.toList());
    }
}
