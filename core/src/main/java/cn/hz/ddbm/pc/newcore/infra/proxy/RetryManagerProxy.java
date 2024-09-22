package cn.hz.ddbm.pc.newcore.infra.proxy;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.infra.RetryManager;

public class RetryManagerProxy implements RetryManager {
    RetryManager retryManager;

    public RetryManagerProxy(RetryManager retryService) {
        this.retryManager = retryService;
    }

    @Override
    public void addTask(FlowContext ctx) {
        retryManager.addTask(ctx);
    }

    @Override
    public Coast.RetryType code() {
        return retryManager.code();
    }
}
