package cn.hz.ddbm.pc.container;

import cn.hz.ddbm.pc.core.support.Container;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.Map;

public class SpringContainer implements Container {
    @Resource
    ApplicationContext context;

    @Override
    public Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return context.getBeansOfType(type);
    }

}
