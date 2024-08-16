package cn.hz.ddbm.pc.core.utils;


import cn.hz.ddbm.pc.core.support.*;
import lombok.Getter;

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

    public static StatisticsSupport getMetricsTemplate() {
        return container.getBean(StatisticsSupport.class);
    }

    public static ExecutorService getPluginExecutorService() {
        return Executors.newFixedThreadPool(3);
    }

    public static Locker getLocker() {
        return container.getBean(Locker.class);
    }


    public static String getDomain() {
        return "";
    }

    public static ExpressionEngine getExpressionEngine() {
        return container.getBean(ExpressionEngine.class);
    }


}
