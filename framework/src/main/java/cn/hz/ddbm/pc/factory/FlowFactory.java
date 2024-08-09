package cn.hz.ddbm.pc.factory;

/**
 * 定义的流程的定义方式
 * 有xml，json，buider等方式。
 */
public interface FlowFactory {
    void loadBeanDefinitions(Resource resource);
}
