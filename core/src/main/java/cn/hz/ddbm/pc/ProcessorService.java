package cn.hz.ddbm.pc;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.newcore.*;
import cn.hz.ddbm.pc.newcore.chaos.LocalChaosAction;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.exception.ActionException;
import cn.hz.ddbm.pc.newcore.exception.SessionException;
import cn.hz.ddbm.pc.newcore.fsm.actions.LocalFsmAction;
import cn.hz.ddbm.pc.newcore.infra.*;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmLocker;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmSessionManager;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmStatisticsSupport;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmStatusManager;
import cn.hz.ddbm.pc.newcore.infra.proxy.*;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaAction;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProcessorService {
    Map<String, BaseFlow> flows;
    PluginService         pluginService;

    public ProcessorService() {
        this.flows         = new HashMap<>();
        this.pluginService = new PluginService();
    }


    public BaseFlow getFlow(String flowName) {
        return flows.get(flowName);
    }

    public FlowContext<SagaState> getSagaContext(String flowName, Object o) {
        return null;
    }

    public FlowContext<SagaState> getFsmContext(String flowName, Object o) {
        return null;
    }

    public static <T> T getAction(Class<T> action) {
        Assert.notNull(action, "action is null");
        if (EnvUtils.isChaos()) {
            if (LocalFsmAction.class.isAssignableFrom(action) || LocalSagaAction.class.isAssignableFrom(action)) {
                return (T) SpringUtil.getBean(LocalChaosAction.class);
            } else {
                return SpringUtil.getBean(Coast.REMOTE_CHAOS_ACTION);
            }
        } else {
            return SpringUtil.getBean(action);
        }
    }

    /**
     * 连续执行
     *
     * @param ctx
     * @throws ActionException
     */
    public void execute(FlowContext ctx) throws ActionException {
        BaseFlow flow = ctx.getFlow();
        while (flow.isRunnable(ctx)) {
            try {
                flow.execute(ctx);
            } catch (RuntimeException e) {                //运行时异常中断
                flushState(ctx);
                flushSession(ctx);
                throw e;
            } catch (Exception e) {                       //其他尝试重试
                flushState(ctx);
                flushSession(ctx);
                addRetryTask(ctx);
            }
        }
    }

    private void flushState(FlowContext ctx) {
        //todo
    }

    private void flushSession(FlowContext ctx) {
        //todo
    }

    private void addRetryTask(FlowContext ctx) {
        //todo
    }


    public void metricsNode(FlowContext ctx) {
        String            flowName = ctx.getFlow().name();
        Serializable      id       = ctx.getId();
        State             state    = ctx.getState();
        StatisticsSupport ss       = getStatisticsSupport(ctx.getFlow());
        ss.increment(flowName, id, state, Coast.STATISTICS.EXECUTE_TIMES);
    }


    public Long getExecuteTimes(FlowContext ctx) {
        String            flowName = ctx.getFlow().name();
        Serializable      id       = ctx.getId();
        State             state    = ctx.getState();
        StatisticsSupport ss       = getStatisticsSupport(ctx.getFlow());
        return ss.get(flowName, id, state, Coast.STATISTICS.EXECUTE_TIMES);
    }

    private StatisticsSupport getStatisticsSupport(BaseFlow flow) {
        return null;
    }


}


