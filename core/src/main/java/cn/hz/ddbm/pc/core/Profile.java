package cn.hz.ddbm.pc.core;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
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
    private SessionManager.Type           sessionManager;
    private StatusManager.Type            statusManager;
    private Map<String, StepAttrs>        states;
    private Map<String, ActionAttrs>      actions;
    private Map<String, ExpressionRouter> routers;

    public Profile(SessionManager.Type sessionManager, StatusManager.Type statusManager) {
        this.actions            = new HashMap<>();
        this.states             = new HashMap<>();
        this.routers            = new HashMap<>();
        this.sessionManager     = sessionManager == null ? SessionManager.Type.redis : sessionManager;
        this.statusManager      = statusManager == null ? StatusManager.Type.redis : statusManager;
    }

    public static Profile defaultOf() {
        return new Profile(SessionManager.Type.redis, StatusManager.Type.redis);
    }

    public static Profile chaosOf() {
        return new Profile(SessionManager.Type.memory, StatusManager.Type.memory);
    }

    public static Profile devOf() {
        return new Profile(SessionManager.Type.memory, StatusManager.Type.memory);
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
