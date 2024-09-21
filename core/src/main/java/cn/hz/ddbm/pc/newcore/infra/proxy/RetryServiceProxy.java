package cn.hz.ddbm.pc.newcore.infra.proxy;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.infra.RetryService;

public class RetryServiceProxy implements RetryService {
    RetryService retryService;

    public RetryServiceProxy(RetryService retryService) {
        this.retryService = retryService;
    }

    @Override
    public void addTask(FlowContext ctx) {
        retryService.addTask(ctx);
    }

    @Override
    public Coast.RetryType code() {
        return retryService.code();
    }
}
