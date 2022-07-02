package io.ddbm.pc;

public class Coast {
    public static final Integer DEFAULT_RETRY = 10;
    public static final String  DEFAULT_EVENT = "next";
    public static final String  TOTAL_ERROR   = "total_error";
    public static final String  TOTAL_COUNT   = "total_count";

    public static String EVENT_COUNT(Event event) {
        return String.format("%s_%s_%s", event.getOn().name, event.getEvent(), "count");
    }
}
