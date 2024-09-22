package cn.hz.ddbm.pc.newcore;

import cn.hz.ddbm.pc.newcore.config.Coast;

import java.util.Objects;


public class StateAttrs {

    FlowAttrs flowAttrs;
    Integer   retry;

    public Integer getRetry() {
        if (null != retry) {
            return retry;
        } else if (null != flowAttrs && null != flowAttrs.getRetryTimes()) {
            return flowAttrs.getRetryTimes();
        } else {
            return Coast.DEFAULT_RETRY_TIMES;
        }
    }

    public static StateAttrs ChaosOf() {
        StateAttrs s = new StateAttrs();
        s.retry = 10;
        return s;
    }
    public static StateAttrs defaultOf() {
        StateAttrs s = new StateAttrs();
        s.retry = 0;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateAttrs that = (StateAttrs) o;
        return Objects.equals(retry, that.retry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(retry);
    }
}
