package cn.hz.ddbm.pc.factory;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.support.Container;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定义的流程的定义方式
 * 有xml，json，buider等方式。
 */
public abstract class FlowFactory<R extends Resource>   {

    public abstract ResourceLoader<R> resourceLoader();

    @PostConstruct
    public List<Flow> loadFlowByResource(Container container) {
        return resourceLoader().loadResources(container).stream().map(Resource::resolve).collect(Collectors.toList());
    }
}
