package cn.hz.ddbm.pc.newcore;

import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FlowAttrs {
    String                  namespace;
    Integer                 maxLoop;
    Integer                 statusTimeout;
    Integer                 lockTimeout;
    Integer                 retryTimes;
    Coast.StatusType        status;
    Coast.RetryType         retry;
    Coast.SessionType       session;
    Coast.LockType          lock;
    Coast.StatisticsType    statistics;
    Coast.ScheduleType      schedule;
    List<Plugin>            plugins;
    Map<String, StateAttrs> stateAttrs;

    public StateAttrs getStateAttrs(String stateCode) {
        if (EnvUtils.isChaos()) {
            return StateAttrs.ChaosOf();
        } else {
            if (null == stateAttrs) {
                return StateAttrs.defaultOf();
            } else {
                return stateAttrs.getOrDefault(stateCode, StateAttrs.defaultOf());
            }
        }
    }
}
