package cn.hz.ddbm.pc.container;

import cn.hutool.core.util.ReflectUtil;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.profile.chaos.ChaosRule;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 1,对其中的方法进行字节码增强，注入混沌
 * 2，考虑解决方案
 * 2.1 自研（chaosAction……等直接生产混沌异常）
 * 2.2 byteman/chaosblade/。。。。等第三方组件。
 */
public class ChaosContainer implements Container {
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
