package cn.hz.ddbm.pc.core;

public interface State {
    String status();

    Integer getRetry();

    interface Instant extends State {
        @Override
        default Integer getRetry() {
            return Integer.MAX_VALUE;
        }
    }

    interface Persist extends State {

    }
}
