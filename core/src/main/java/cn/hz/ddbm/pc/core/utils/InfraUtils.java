package cn.hz.ddbm.pc.core.utils;


import cn.hutool.core.lang.Pair;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.support.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class InfraUtils {


    @Getter
    static Container                   container;
    static Map<String, SessionManager> sessionManagerMap;
    static Map<String, StatusManager>  statusManagerMap;

    public InfraUtils(Container container) {
        InfraUtils.container = container;
        sessionManagerMap    = container.getBeansOfType(SessionManager.class).values().stream().collect(Collectors.toMap(
                SessionManager::code,
                t -> t
        ));
        statusManagerMap     = container.getBeansOfType(StatusManager.class).values().stream().collect(Collectors.toMap(
                StatusManager::code,
                t -> t
        ));
    }

    public static SessionManager getSessionManager(String code) {
        return sessionManagerMap.get(code);
    }

    public static StatusManager getStatusManager(String code) {
        return statusManagerMap.get(code);
    }

    public static MetricsTemplate getMetricsTemplate() {
        return container.getBean(MetricsTemplate.class);
    }

    public static ExecutorService getPluginExecutorService() {
        return Executors.newFixedThreadPool(3);
    }

    public static Pair<Flow.STAUS, String> syncStatus(String name, Serializable id) {
        return null;
    }

    public static Map<String, Plugin> getPluginBeans() {
        return container.getBeansOfType(Plugin.class).values().stream().collect(Collectors.toMap(
                Plugin::code,
                t -> t
        ));
    }

    public static Action getActionBean(String action) {
        return container.getBean(action, Action.class);
    }


    //如果持有锁，续锁，如果没有，新建，返回true
    //拿不到锁，返回false。
    public static Boolean tryLock(String key, int seconds) {
        return true;
    }

    public static Boolean releaseLock(String key) {
        return null;
    }

    public static String getDomain() {
        return "";
    }

    public static ExpressionEngine getExpressionEngine() {
        return null;
    }


    public static ActionRouter getActionRouter(Action action, Router router) {
        return new ActionRouter(action, router);
    }

}
