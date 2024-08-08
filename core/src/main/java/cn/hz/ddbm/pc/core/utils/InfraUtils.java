package cn.hz.ddbm.pc.core.utils;


import cn.hutool.core.lang.Pair;
import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.Router;
import cn.hz.ddbm.pc.core.support.MetricsWindows;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class InfraUtils {
    static Logger lockLog = LoggerFactory.getLogger("");

    public static SessionManager getSessionManager() {
        return null;
    }

    public static StatusManager getStatusManager() {
        return null;
    }

    public static MetricsWindows getNodeMetricsWindows(String node, Date date) {
        return null;
    }

    public static ExecutorService getPluginExecutorService() {
        return null;
    }

    public static Pair<Flow.STAUS, String> syncStatus(String name, Serializable id) {
        return null;
    }

    public static List<Plugin> getPluginBeans(List<String> plugins) {
        return null;
    }

    public static Action getActionBean(String action) {
        return null;
    }

    public static Router getRouterBean(String router) {
        return null;
    }
    //如果持有锁，续锁，如果没有，新建，返回true
    //拿不到锁，返回false。
    public static Boolean tryLock(String key, int seconds) {
        return null;
    }
    public static Boolean releaseLock(String key) {
        return null;
    }
}
