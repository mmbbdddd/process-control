package io.ddbm.pc.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware {
    static ApplicationContext ctx;

    public static <T> T getBean(String name, Class<T> type) {
        return ctx.getBean(name, type);
    }

    public static <T> T getBean(Class<T> type) {
        return ctx.getBean(type);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.ctx = applicationContext;
    }
}
