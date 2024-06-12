package hz.ddbm.pc.core.config;

import hz.ddbm.pc.core.domain.Transition;

public class Coast {
    public static final String EVENT_PUSH            = "push";
    public static final String ACTION_DO_NOTHING     = "push";
    public static final String ROUTER_DO_NOTHING     = "none://none";
    public static final String SESSION_SERVICE_REDIS = "redisSessionService";
    public static final String STATUS_SERVICE_REDIS  = "redisSessionService";
    public static final String SESSION_SERVICE_JVM   = "jvmSessionService";
    public static final String IS_CHAOS              = "is_chaos";


    public static final Transition TRANSITION_PUSH_NOTHING = new Transition(new TransitionProperties(
            EVENT_PUSH, ACTION_DO_NOTHING, ROUTER_DO_NOTHING, null, null
    ));
}
