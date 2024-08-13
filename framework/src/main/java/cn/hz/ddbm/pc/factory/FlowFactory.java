package cn.hz.ddbm.pc.factory;

import cn.hz.ddbm.pc.core.Flow;

import java.util.List;

/**
 * 定义的流程的定义方式
 * 有xml，json，buider等方式。
 */
public interface FlowFactory<R extends Resource> {
    List<Flow> loadBeanDefinitions(ResourceLoader<R> resource);
}
