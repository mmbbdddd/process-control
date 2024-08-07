package hz.ddbm.pc.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import hz.ddbm.pc.core.service.SessionService;
import hz.ddbm.pc.core.service.StatusService;
import hz.ddbm.pc.core.config.Coast;
import hz.ddbm.pc.core.service.session.JvmSessionService;
import hz.ddbm.pc.core.service.session.RedisSessionService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

public  class InfraUtils implements BeanFactoryPostProcessor, ApplicationContextAware {
    private static ConfigurableListableBeanFactory beanFactory;
    private static ApplicationContext              applicationContext;

    public InfraUtils() {
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        InfraUtils.beanFactory = beanFactory;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        InfraUtils. applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static ListableBeanFactory getBeanFactory() {
        ListableBeanFactory factory = null == beanFactory ? applicationContext : beanFactory;
        if (null == factory) {
            throw new RuntimeException("No ConfigurableListableBeanFactory or ApplicationContext injected, maybe not in the Spring environment?");
        } else {
            return (ListableBeanFactory)factory;
        }
    }

    public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws RuntimeException {
        ConfigurableListableBeanFactory factory;
        if (null != beanFactory) {
            factory = beanFactory;
        } else {
            if (!(applicationContext instanceof ConfigurableApplicationContext)) {
                throw new RuntimeException("No ConfigurableListableBeanFactory from context!");
            }

            factory = ((ConfigurableApplicationContext)applicationContext).getBeanFactory();
        }

        return factory;
    }


    public static <T> T getBean(Class<T> clazz) {
        return getBeanFactory().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getBeanFactory().getBean(name, clazz);
    }

    public static <T> T getBean(TypeReference<T> reference) {
        ParameterizedType parameterizedType = (ParameterizedType)reference.getType();
        Class<T>          rawType           = (Class)parameterizedType.getRawType();
        Class<?>[] genericTypes = (Class[]) Arrays.stream(parameterizedType.getActualTypeArguments()).map((type) -> {
            return (Class)type;
        }).toArray((x$0) -> {
            return new Class[x$0];
        });
        String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
        return getBean(beanNames[0], rawType);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    public static String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    public static String getProperty(String key) {
        return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key, targetType, defaultValue);
    }

    public static String getApplicationName() {
        return getProperty("spring.application.name");
    }

    public static String[] getActiveProfiles() {
        return null == applicationContext ? null : applicationContext.getEnvironment().getActiveProfiles();
    }



    public static <T> void registerBean(String beanName, T bean) {
        ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        factory.autowireBean(bean);
        factory.registerSingleton(beanName, bean);
    }

    public static void unregisterBean(String beanName) {
        ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        if (factory instanceof DefaultSingletonBeanRegistry) {
            DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry)factory;
            registry.destroySingleton(beanName);
        } else {
            throw new RuntimeException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
        }
    }

    public static void publishEvent(ApplicationEvent event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }

    }

    public static void publishEvent(Object event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }
    }


    /////

    /**
     * 1，如果application.propierites.pc.session.provider = redis/jvm指定，则使用指定的。
     * 2，检测是否存在RedisSessionService
     * 3，检测是否存在JvmSessionService
     * 4，都不存在则使用3
     *
     * @return
     */
    public static SessionService getSessionService() {
        String             provider = getApplicationContext().getEnvironment().getProperty("pc.session.provider");
        if (StringUtils.isEmpty(provider)) {
            return getSessionServiceAuto();
        } else {
            return getSessionServiceSpecial(provider);
        }
    }

    private static SessionService getSessionServiceSpecial(String provider) {
        switch (provider) {
            case Coast.SESSION_SERVICE_REDIS:
                return getApplicationContext().getBean(RedisSessionService.class);
            case Coast.SESSION_SERVICE_JVM:
                return getApplicationContext().getBean(JvmSessionService.class);
            default:
                return getApplicationContext().getBean(JvmSessionService.class);
        }
    }

    private static SessionService getSessionServiceAuto() {
        SessionService     bean = null;
        try {
            bean = getApplicationContext().getBean(RedisSessionService.class);
        } catch (NoSuchBeanDefinitionException e) {
            try {
                bean = getApplicationContext().getBean(JvmSessionService.class);
            } catch (NoSuchBeanDefinitionException e2) {
                bean = new JvmSessionService();
            }
        }
        return bean;
    }


    /**
     * 如果有用户自定义实现，则使用用户自定义实现。否则，使用缺省实现。
     *
     * @return
     */
    public static StatusService getStatusService(String statusService) {
        if (!StringUtils.isEmpty(statusService)) {
            return getApplicationContext().getBean(statusService, StatusService.class);
        } else {
            return (StatusService) getSessionService();
        }
    }
}
