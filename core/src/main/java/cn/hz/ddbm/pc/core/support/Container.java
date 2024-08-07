package cn.hz.ddbm.pc.core.support;


import java.util.Map;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 21:25
 * @Version 1.0.0
 **/


public interface Container {
    <T> T getBean(Class<T> clazz);

    <T> T getBean(String name, Class<T> clazz);

    <T> Map<String, T> getBeansOfType(Class<T> type);


    <E> void publishEvent(E event);
}
