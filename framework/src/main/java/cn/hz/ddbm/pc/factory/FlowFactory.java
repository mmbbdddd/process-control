package cn.hz.ddbm.pc.factory;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.support.Container;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定义的流程的定义方式
 * 有xml，json，buider等方式。
 */
public class FlowFactory<R extends Resource> {
    @javax.annotation.Resource
    ResourceLoader<R> resourceLoader;
    @javax.annotation.Resource
    Container         container;

    @PostConstruct
    public List<Flow> loadFlowByResource() {
        return resourceLoader.loadResources(container).stream().map(t -> {
            try {
                return t.resolve();
            } catch (Exception e) {
                Logs.error.error("", e);
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
