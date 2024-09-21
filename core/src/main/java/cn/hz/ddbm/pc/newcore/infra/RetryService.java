package cn.hz.ddbm.pc.newcore.infra;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.config.Coast;

public interface RetryService extends ValueObject<Coast.RetryType> {
    void addTask(FlowContext ctx);
}
