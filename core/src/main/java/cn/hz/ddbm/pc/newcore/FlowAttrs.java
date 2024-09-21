package cn.hz.ddbm.pc.newcore;

import cn.hz.ddbm.pc.newcore.config.Coast;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FlowAttrs {
    String                  namespace;
    Integer                 maxLoop;
    Integer                 statusTimeout;
    Integer                 lockTimeout;
    Coast.StatusType        status;
    Coast.RetryType         retry;
    Coast.SessionType       session;
    Coast.LockType          lock;
    Coast.StatisticsType    statistics;
    Coast.ScheduleType      schedule;
    List<Plugin>            plugins;
    Map<String, StateAttrs> stateAttrs;
}
