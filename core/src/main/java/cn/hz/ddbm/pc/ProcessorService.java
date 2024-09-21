package cn.hz.ddbm.pc;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.newcore.*;
import cn.hz.ddbm.pc.newcore.chaos.LocalChaosAction;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.exception.ActionException;
import cn.hz.ddbm.pc.newcore.exception.SessionException;
import cn.hz.ddbm.pc.newcore.factory.FlowFactory;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
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

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProcessorService {
    Map<String, BaseFlow> flows;
    PluginService         pluginService;

    @PostConstruct
    public void init() {
        this.pluginService = new PluginService();
        this.flows = new HashMap<>();
        SpringUtil.getBeansOfType(FlowFactory.class).forEach((k, b) -> {
            this.flows.putAll(b.getFlows());
        });
    }


    public BaseFlow getFlow(String flowName) {
        return flows.get(flowName);
    }

    public FlowContext<SagaState> getContext(String flowName, Payload payload, String event, Boolean fluent) {
        return new FlowContext<>(getFlow(flowName), payload, event, getSession(flowName, payload.getId()), fluent);
    }

    public Map<String, Object> getSession(String flow, String id) {
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
                Long executeTimes = getExecuteTimes(ctx);
                Integer retryTimes = ctx.flow.stateAttrs(ctx.getState()).getRetry();
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


    private StatusManager getStatusManager(BaseFlow flow) {
        return new StatusManagerProxy(getByCode(flow.flowAttrs().getStatus(), StatusManager.class));
    }


    private SessionManager getSessionManager(BaseFlow flow) {
        return new SessionManagerProxy(getByCode(flow.flowAttrs().getSession(), SessionManager.class));
    }

    private RetryService getRetryService(BaseFlow flow) {
        return new RetryServiceProxy(getByCode(flow.flowAttrs().getRetry(), RetryService.class));
    }

    private Locker getLocker(BaseFlow flow) {
        return new LockProxy(getByCode(flow.flowAttrs().getLock(), Locker.class));
    }

    private StatisticsSupport getStatisticsSupport(BaseFlow flow) {
        return new StatisticsSupportProxy(getByCode(flow.flowAttrs().getStatistics(), StatisticsSupport.class));
    }

    private <T extends ValueObject> T getByCode(Enum code, Class<T> type) {
        return SpringUtil.getBeansOfType(type)
                         .entrySet()
                         .stream()
                         .filter(e -> e.getValue().code().equals(code))
                         .findFirst()
                         .get()
                         .getValue();
    }


}


