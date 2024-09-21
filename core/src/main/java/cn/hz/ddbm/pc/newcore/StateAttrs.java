package cn.hz.ddbm.pc.newcore;

import cn.hz.ddbm.pc.newcore.config.Coast;


public class StateAttrs {

    FlowAttrs flowAttrs;
    Integer   retry;


    public static StateAttrs defaultOf() {
        StateAttrs s = new StateAttrs();
        s.retry = 0;
        return s;
    }

    public static StateAttrs ChaosOf() {
        StateAttrs s = new StateAttrs();
        s.retry = 10;
        return s;
    }

    public Integer getRetry() {
        if (null != retry) {
            return retry;
        } else if (null != flowAttrs && null != flowAttrs.getRetryTimes()) {
            return flowAttrs.getRetryTimes();
        } else {
            return Coast.DEFAULT_RETRY_TIMES;
        }
    }


}
