package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Profile {
    private Integer                       retry         = Coasts.DEFAULT_RETRY;
    private Integer                       statusTimeout = Coasts.DEFAULT_STATUS_TIMEOUT;
    private Integer                       lockTimeout   = Coasts.DEFAULT_LOCK_TIMEOUT;
    private SessionManager                sessionManagerBean;
    private StatusManager                 statusManagerBean;
    private String                        sessionManager;
    private String                        statusManager;
    private Map<String, StepAttrs>        states;
    private Map<String, ActionAttrs>      actions;
    private Map<String, ExpressionRouter> routers;
    private Container                     container;

    public Profile(Container container, String sessionManager, String statusManager) {
        Assert.notNull(container, "container is null");
        this.container          = container;
        this.actions            = new HashMap<>();
        this.states             = new HashMap<>();
        this.routers            = new HashMap<>();
        this.sessionManager     = sessionManager == null ? Coasts.SESSION_MANAGER_REDIS : sessionManager;
        this.statusManager      = statusManager == null ? Coasts.STATUS_MANAGER_REDIS : statusManager;
        this.sessionManagerBean = InfraUtils.getSessionManager(sessionManager);
        this.statusManagerBean  = InfraUtils.getStatusManager(statusManager);
    }

    public static Profile defaultOf() {
        return new Profile(InfraUtils.getContainer(), Coasts.SESSION_MANAGER_REDIS, Coasts.STATUS_MANAGER_REDIS);
    }

    public static Profile chaosOf(Container container) {
        return new Profile(container, Coasts.SESSION_MANAGER_MEMORY, Coasts.STATUS_MANAGER_MEMORY);
    }

    public static Profile devOf() {
        return new Profile(InfraUtils.getContainer(), Coasts.SESSION_MANAGER_MEMORY, Coasts.STATUS_MANAGER_MEMORY);
    }

    public void setStepAttrs(String state, StepAttrs stepAttrs) {
        stepAttrs.profile = this;
        this.states.put(state, stepAttrs);
    }

    public StepAttrs getStepAttrsOrDefault(String state) {
        return this.states.getOrDefault(state, new StepAttrs(this));
    }

    public void addRouter(ExpressionRouter router) {
        this.routers.put(router.routerName(), router);
    }

    public ExpressionRouter getRouter(String routerName) {
        return this.routers.get(routerName);
    }


    public static class StepAttrs {
        Profile profile;
        Integer retry;

        public StepAttrs(Profile profile) {
            this.profile = profile;
        }

        public Integer getRetry() {
            if (null == retry) {
                return profile.getRetry();
            }
            return retry;
        }
    }

    @Data
    public static class ActionAttrs {
        Integer failover;

    }
}
