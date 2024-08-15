package cn.hz.ddbm.pc.core.support;


import cn.hz.ddbm.pc.core.ValueObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 21:25
 * @Version 1.0.0
 **/


public interface Container {
    Object getBean(String beanName);
    <T> T getBean(Class<T> clazz);

    <T> T getBean(String name, Class<T> clazz);

    <T> Map<String, T> getBeansOfType(Class<T> type);

    default <T extends ValueObject> List<T> getByCodesOfType(List<String> codes, Class<T> type) {
        Map<String, T> beans = getBeansOfType(type);
        return beans.values().stream().filter(t -> codes.contains(t.code())).collect(Collectors.toList());
    }

    default <T extends ValueObject> T getByCodeOfType(String code, Class<T> type) {
        Map<String, T> beans = getBeansOfType(type);
        return beans.values().stream().filter(t -> code.contains(t.code())).findFirst().get();
    }

}
