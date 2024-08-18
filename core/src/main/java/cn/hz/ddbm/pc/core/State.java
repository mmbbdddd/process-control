package cn.hz.ddbm.pc.core;

public interface State<S extends Enum<S>> {
    S status();

    Integer getRetry();

    interface Instant<S extends Enum<S>> extends State<S> {
        @Override
        default Integer getRetry() {
            return Integer.MAX_VALUE;
        }
    }

    interface Persist<S extends Enum<S>> extends State<S> {

    }
}
