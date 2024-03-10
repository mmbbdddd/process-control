package io.ddbm.pc.config;

import io.ddbm.pc.Action;


public class Coast {
    public static final String REDIS_SESSION = "redis";

    public static final Integer DEFAULT_RETRY = 10;

    public static final String DEFAULT_EVENT = "submit";

    public static final String CANCEL_EVENT = "cannel";

    public static final String PAUSE_EVENT = "pause";

    public static final Action NONE_ACTION = new NoneAction();

    public static final String NODE_ROUTER_PREFIX = "node";

    public static final String FLOW_ROUTER_PREFIX = "flow";

    public static final String EXPRESSION_ROUTER_PREFIX = "router";

    public static final String NONE_ACTION_NAME = "none_action";

    public static final Long DEFAULT_WEIGHT = 100l;

    public static final String FLOW_STATUS_FIELD = "flow_status";

    public static final String NODE_STATUS_FIELD = "node_status";
}
