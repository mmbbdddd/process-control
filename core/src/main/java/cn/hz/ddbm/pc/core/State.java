package cn.hz.ddbm.pc.core;

public interface State {
    String status();

    default Integer getRetry() {
        return Integer.MAX_VALUE;
    }

    interface Instant extends State {

    }

    interface Persist extends State {

    }
}
