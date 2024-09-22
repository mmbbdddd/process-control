package cn.hz.ddbm.pc.newcore;

import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;
import lombok.Data;

import java.util.ArrayList;
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

    public static FlowAttrs chaosOf() {
        FlowAttrs f = new FlowAttrs();
        f.namespace     = "app";
        f.maxLoop       = 3;
        f.statusTimeout = 3000;
        f.lockTimeout   = 3000;
        f.status        = Coast.StatusType.jvm;
        f.session       = Coast.SessionType.jvm;
        f.lock          = Coast.LockType.jvm;
        f.statistics    = Coast.StatisticsType.simple;
        f.schedule      = Coast.ScheduleType.timer;
        f.plugins       = new ArrayList<>();
        return f;
    }

    public static FlowAttrs defaultOf() {
        FlowAttrs f = new FlowAttrs();
        f.namespace     = "app";
        f.maxLoop       = 3;
        f.statusTimeout = 3000;
        f.lockTimeout   = 3000;
        f.status        = Coast.StatusType.redis;
        f.session       = Coast.SessionType.redis;
        f.lock          = Coast.LockType.redis;
        f.statistics    = Coast.StatisticsType.simple;
        f.schedule      = Coast.ScheduleType.timer;
        f.plugins       = new ArrayList<>();
        return f;
    }


}
