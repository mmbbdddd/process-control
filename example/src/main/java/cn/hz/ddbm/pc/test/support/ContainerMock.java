package cn.hz.ddbm.pc.test.support;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.support.Container;

import java.util.Map;

public class ContainerMock implements Container {
    @Override
    public <T> T getBean(Class<T> clazz) {
        return SpringUtil.getBean(clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return SpringUtil.getBean(name, clazz);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return SpringUtil.getBeansOfType(type);
    }

    @Override
    public <E> void publishEvent(E event) {

    }
}
