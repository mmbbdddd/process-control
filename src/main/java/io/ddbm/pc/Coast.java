package io.ddbm.pc;

public class Coast {
    public static final Integer DEFAULT_RETRY = 10;
    public static final String  DEFAULT_EVENT = "next";
    public static final String  TOTAL_ERROR   = "total_error";
    public static final String  TOTAL_COUNT = "total_count";
    public static final String INTERRUPT      = "interrupt";
    public static final String INTERRUPT_MESSAGE = "interrupt_message";

    public static String EVENT_COUNT(String node,String event) {
        return String.format("%s_%s_%s", node, event, "count");
    }
}
