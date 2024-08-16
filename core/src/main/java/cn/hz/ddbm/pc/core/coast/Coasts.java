package cn.hz.ddbm.pc.core.coast;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.action.NoneAction;

public class Coasts {
    public static final String  NODE_RETRY             = "retry";
    public static final String  PLUGIN_DIGEST_LOG      = "digest";
    public static final String  PLUGIN_ERROR_LOG       = "error";
    public static final String  EVENT_DEFAULT          = "push";
    public static final String  EVENT_CANCEL           = "cancel";
    public static final String  EVENT_PAUSE            = "pause";
    public static final String  NONE                   = "none";
    public static final Action  NONE_ACTION            = new NoneAction();
    public static final Integer DEFAULT_RETRY          = 1;
    public static final Integer DEFAULT_STATUS_TIMEOUT = 3000;
    public static final Integer DEFAULT_LOCK_TIMEOUT   = 3000;
    public static final String  PC_SERVICE_CHAOS       = "chaosPcService";
    public static       String  SESSION_MANAGER        = "session_manager";
    public static       String  STATUS_MANAGER         = "status_manager";
    public static       String  SESSION_MANAGER_MEMORY = "memory";
    public static       String  SESSION_MANAGER_REDIS  = "redis";
    public static       String  STATUS_MANAGER_MEMORY  = "memory";
    public static       String  STATUS_MANAGER_REDIS   = "redis";
    public static       String  STATUS_MANAGER_DAO     = "dao";

    public static class DEFAULT_STATUS {
        public static String SU     = "su";
        public static String FAIL   = "fail";
        public static String ERROR  = "error";
        public static String CANCEL = "cancel";
        public static String PAUSE  = "pause";
    }
}
