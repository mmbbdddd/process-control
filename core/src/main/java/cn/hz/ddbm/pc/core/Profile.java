package cn.hz.ddbm.pc.core;

import cn.hz.ddbm.pc.core.coast.Coasts;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Profile {
    private String                   sessionManager;
    private String                   statusManager;
    private Integer                  retry = Coasts.DEFAULT_RETRY;
    private Map<String, StepAttrs>   states;
    private Map<String, ActionAttrs> actions;

    public Profile() {
        this.actions = new HashMap<>();
        this.states  = new HashMap<>();
    }

    public static Profile defaultOf() {
        return new Profile();
    }

    public void setStepAttrs(String state, StepAttrs stepAttrs) {
        stepAttrs.profile = this;
        this.states.put(state, stepAttrs);
    }

    public StepAttrs getStepAttrsOrDefault(String state) {
        return this.states.getOrDefault(state, new StepAttrs(this));
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
