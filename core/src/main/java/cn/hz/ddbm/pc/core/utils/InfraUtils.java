package cn.hz.ddbm.pc.core.utils;


import cn.hutool.core.lang.Pair;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.support.MetricsWindows;
import cn.hz.ddbm.pc.core.Flow;

import java.io.Serializable;
import java.util.Date;

public class InfraUtils {

    public static SessionManager getSessionManager() {
        return null;
    }

    public static StatusManager getStatusManager() {
        return null;
    }

    public static MetricsWindows getMetricsWindows(String obj, Date date) {
        return null;
    }

    public static Pair<Flow.STAUS, String> syncStatus(String name, Serializable id) {
        return null;
    }
}
