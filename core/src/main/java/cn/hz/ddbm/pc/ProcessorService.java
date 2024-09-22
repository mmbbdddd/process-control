package cn.hz.ddbm.pc;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.newcore.BaseFlow;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.Payload;
import cn.hz.ddbm.pc.newcore.chaos.LocalChaosAction;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.exception.FlowEndException;
import cn.hz.ddbm.pc.newcore.exception.FlowStatusException;
import cn.hz.ddbm.pc.newcore.factory.FlowFactory;
import cn.hz.ddbm.pc.newcore.fsm.actions.LocalFsmAction;
import cn.hz.ddbm.pc.newcore.infra.*;
import cn.hz.ddbm.pc.newcore.infra.proxy.*;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaAction;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class ProcessorService {
    Map<String, BaseFlow> flows;
    PluginService         pluginService;

    @PostConstruct
    public void init() {
        this.pluginService = new PluginService();
        this.flows         = new HashMap<>();
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
     */
    public void execute(FlowContext ctx)   {
        BaseFlow flow = ctx.getFlow();
        while (true) {
            try {
                flow.execute(ctx);
            } catch (RuntimeException e) {                //运行时异常中断
                flushState(ctx);
                flushSession(ctx);
                throw e;
            } catch (Exception e) {                       //其他尝试重试
                flushState(ctx);
                flushSession(ctx);
                if (e instanceof FlowStatusException || e instanceof FlowEndException) {

                } else {
                    addRetryTask(ctx);
                }
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


    private StatusManager getStatusManager(BaseFlow flow) {
        return new StatusManagerProxy(getByCode(flow.flowAttrs().getStatus(), StatusManager.class));
    }


    private SessionManager getSessionManager(BaseFlow flow) {
        return new SessionManagerProxy(getByCode(flow.flowAttrs().getSession(), SessionManager.class));
    }

    private RetryManager getRetryService(BaseFlow flow) {
        return new RetryManagerProxy(getByCode(flow.flowAttrs().getRetry(), RetryManager.class));
    }

    private LockManager getLocker(BaseFlow flow) {
        return new LockManagerProxy(getByCode(flow.flowAttrs().getLock(), LockManager.class));
    }

    private StatisticsManager getStatisticsSupport(BaseFlow flow) {
        return new StatisticsSupportProxy(getByCode(flow.flowAttrs().getStatistics(), StatisticsManager.class));
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


