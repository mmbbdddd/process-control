package cn.hz.ddbm.pc.profile;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.*;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.exception.*;
import cn.hz.ddbm.pc.core.log.Logs;
import cn.hz.ddbm.pc.core.utils.InfraUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PcService {
    Map<String, Fsm> flows = new HashMap<>();


    public <T extends FlowPayload> void batchExecute(String flowName, List<T> payloads, String event, Profile profile) throws StatusException, SessionException {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payloads, "FlowPayload is null");
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Fsm flow = flows.get(flowName);

        for (T payload : payloads) {
            FlowContext<T> ctx = new FlowContext<>(flow, payload, event, profile);
            execute(ctx);
        }
    }


    public <T extends FlowPayload> void execute(String flowName, T payload, String event, Profile profile) throws StatusException, SessionException {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payload, "FlowPayload is null");
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Fsm            flow = flows.get(flowName);
        FlowContext<T> ctx  = new FlowContext<>(flow, payload, event, profile);
        execute(ctx);
    }

    public <T extends FlowPayload> void execute(FlowContext<T> ctx) throws StatusException, SessionException {

        try {
            if (Boolean.FALSE.equals(tryLock(ctx))) {
                return;
            }

            Boolean fluent = ctx.getFluent();
            ctx.getFlow().execute(ctx);
            if (fluent && isCanContinue(ctx)) {
                ctx.setEvent(Event.of(Coasts.EVENT_DEFAULT));
                ctx.getFlow().execute(ctx);
            }
        } catch (ActionException e) {
            //io异常 = 可重试
            if (e.getRaw() instanceof IOException) {
                Logs.error.error("{},{}", ctx.getFlow().getName(), ctx.getId(), e);
                flush(ctx);
                execute(ctx);
            }
            //中断流程除（内部错误：不可重复执行，执行次数受限……）再次调度可触发：
            if (e.getRaw() instanceof InterruptedFlowException) {
                Logs.error.error("{},{}", ctx.getFlow().getName(), ctx.getId(), e);
                flush(ctx);
            }
            //中断流程（内部程序错误：配置错误，代码错误）再次调度不响应：
            if (e.getRaw() instanceof PauseFlowException) {
                Logs.error.error("{},{}", ctx.getFlow().getName(), ctx.getId(), e);
                flush(ctx);
            }
        } catch (RouterException e) {
            //即PauseFlowException
            Logs.error.error("{},{}", ctx.getFlow().getName(), ctx.getId(), e.getRaw());
            flush(ctx);
        } finally {
            releaseLock(ctx);
        }

    }

    public void addFlow(Fsm flow) {
        this.flows.put(flow.getName(), flow);
    }

    protected Fsm getFlow(String flowName) {
        return this.flows.get(flowName);
    }

    /**
     * 可继续运行
     * 1，流程状态是Runable状态
     * 2，节点状态类型是非end的
     * 3，运行时限制为false（执行次数限制等）
     *
     * @param ctx
     * @return
     */
    public boolean isCanContinue(FlowContext<?> ctx) {
        Fsm.STAUS flowStatus = ctx.getStatus().getFlow();
        String    node       = ctx.getStatus().getNode();
        String    flowName   = ctx.getFlow().getName();
        State     nodeObj    = ctx.getFlow().getStep(node);
        if (ctx.getFlow().isRouter(node)) {
            return true;
        }
        if (!flowStatus.equals(Fsm.STAUS.RUNNABLE)) {
            Logs.flow.info("流程不可运行：{},{},{}", flowName, ctx.getId(), flowStatus.name());
            return false;
        }
        if (ctx.getFlow().isEnd(node)) {
            Logs.flow.info("流程已结束：{},{},{}", flowName, ctx.getId(), node);
            return false;
        }
        String  windows   = String.format("%s:%s:%s:%s", ctx.getFlow().getName(), ctx.getId(), node, Coasts.NODE_RETRY);
        Long    exeRetry  = InfraUtils.getMetricsTemplate().get(windows);
        Integer nodeRetry = nodeObj.getRetry();

        if (exeRetry > nodeRetry) {
            Logs.flow.info("流程已限流：{},{},{},{}>{}", flowName, ctx.getId(), node, exeRetry, nodeRetry);
            return false;
        }
        return true;
    }

    /**
     * 刷新状态到基础设施
     */
    private void flush(FlowContext<?> ctx) throws SessionException, StatusException {
        InfraUtils.getSessionManager(ctx.getProfile().getSessionManager()).flush(ctx);
        InfraUtils.getStatusManager(ctx.getProfile().getStatusManager()).flush(ctx);
    }


    private void releaseLock(FlowContext<?> ctx) {
        String key = String.format("%s:%s:%s", InfraUtils.getDomain(), ctx.getFlow().getName(), ctx.getId());
        try {
            InfraUtils.getLocker().releaseLock(key);
        } catch (Exception e) {
            //todo
            Logs.error.error("",e);
        }
    }

    private Boolean tryLock(FlowContext<?> ctx) {
        String key = String.format("%s:%s:%s", InfraUtils.getDomain(), ctx.getFlow().getName(), ctx.getId());
        try {
            InfraUtils.getLocker().tryLock(key, ctx.getProfile().getLockTimeout());
            return true;
        } catch (Exception e) {
            Logs.error.error("", e);
            return false;
        }
    }

}
