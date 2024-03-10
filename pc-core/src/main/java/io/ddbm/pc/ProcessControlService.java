package io.ddbm.pc;

import com.google.common.collect.Lists;
import io.ddbm.pc.chaos.ChaosFlowRequest;
import io.ddbm.pc.chaos.ChaosService;
import io.ddbm.pc.exception.ContextCreateException;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.NonRunnableException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.SessionException;
import io.ddbm.pc.factory.FlowFactory;
import io.ddbm.pc.plugins.ChaosModeLogPlugin;
import io.ddbm.pc.status.SessionManager;
import io.ddbm.pc.status.StatusException;
import io.ddbm.pc.status.StatusManager;
import io.ddbm.pc.support.DefaultContext;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;
import io.ddbm.pc.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 流程编排服务
 */
@Component
public class ProcessControlService {

    @Autowired
    ChaosService chaosService;

    /**
     * 运行一步
     *
     * @param request
     * @throws PauseException
     * @throws InterruptException
     * @throws NonRunnableException
     * @throws ContextCreateException
     */
    public void executeOnce(FlowRequest<?> request)
        throws NonRunnableException, PauseException, InterruptException, ParameterException, ContextCreateException,
               SessionException, StatusException, NoSuchNodeException, NoSuchEventException {
        Assert.notNull(request, "request is null");
        Flow flow = FlowFactory.get(request.getFlowName());
        try {
            FlowContext ctx = createContext(request, flow);

            flow.execute(ctx);

            StatusManager.get(flow).updateStatus(request.getFlowName(), request.getDateId(), ctx.getStatus());
            flow.onStateUpdate(ctx);
            //            更新上下文
            SessionManager.get(flow).setSession(request.getFlowName(), request.getDateId(), ctx.getSession());
        } catch (ContextCreateException e) {
            flow.onContextCreateException(e, request);
            throw e;
        } catch (SessionException e) {
            flow.onSessionException(e, request);
            throw e;
        } catch (StatusException e) {
            flow.onStatusException(e, request);
            throw e;
        } catch (Exception e) {
            flow.onOtherIoException(e, request);
            throw e;
        }
    }

    /**
     * 连续运行，直到异常或者结束
     *
     * @param request
     * @throws ContextCreateException
     */
    public void executeFluent(FlowRequest<?> request)
        throws ContextCreateException, ParameterException, PauseException, StatusException, SessionException,
               NoSuchNodeException, NoSuchEventException {
        Assert.notNull(request, "request is null");
        Flow flow = FlowFactory.get(request.getFlowName());
        try {
            FlowContext ctx = createContext(request, flow);
            while (ctx.isRun()) {
                try {
                    ctx.getFlow().execute(ctx);
                } catch (NonRunnableException e) {
                    //流程停止
                    break;
                } catch (NoSuchNodeException | NoSuchEventException e) {
                    //流程停止
                    throw e;
                } catch (ParameterException | PauseException e) {
                    //暂停流程，等用户或者运营重新发起
                    throw e;
                } catch (InterruptException e) {
                    //延迟运行 正常这里应该停止等调度器重新发起。  
                    break;
                }
            }
            //            更新流程状态、节点状态
            StatusManager.get(flow).updateStatus(request.getFlowName(), request.getDateId(), ctx.getStatus());
            ctx.getFlow().onStateUpdate(ctx);
            //            更新上下文
            SessionManager.get(flow).setSession(request.getFlowName(), request.getDateId(), ctx.getSession());
        } catch (ContextCreateException e) {
            flow.onContextCreateException(e, request);
            throw e;
        } catch (SessionException e) {
            flow.onSessionException(e, request);
            throw e;
        } catch (StatusException e) {
            flow.onStatusException(e, request);
            throw e;
        } catch (Exception e) {
            flow.onOtherIoException(e, request);
            throw e;
        }
    }

    public void chaos(Integer times, ChaosFlowRequest request) {
        chaos(times, request, Lists.newArrayList(new ChaosModeLogPlugin()));
    }

    public void chaos(Integer times, ChaosFlowRequest request, List<Plugin> plugins) {
        Assert.notNull(request, "request is null");
        SnowFlake snowFlake = new SnowFlake(2, 5);
        for (int i = 0; i < times; i++) {
            request.setDateId(snowFlake.nextId());
            Flow flow = FlowFactory.getForChaos(request.getFlowName());
            flow.setPlugins(plugins);
            FlowContext ctx = null;
            try {
                ctx = createChaosContext(request, flow);

                while (ctx.isRun()) {
                    try {
                        ctx.getFlow().execute(ctx);
                    } catch (NonRunnableException | NoSuchEventException | NoSuchNodeException e) {
                        //流程结束，程序停止
                        break;
                    } catch (Throwable e) {
                        //                        e.printStackTrace();
                        //继续流程，等用户或者运营重新发起
                        chaosService.count(flow.getName(), request.getDateId(), ctx.getNode(), request.getEvent(),
                            e.getClass().getSimpleName());
                    }
                }
                //            更新流程状态、节点状态
                StatusManager.getForChaos(flow).updateStatus(request.getFlowName(), request.getDateId(),
                    ctx.getStatus());
                ctx.getFlow().onStateUpdate(ctx);
                //            更新上下文
                SessionManager.getForChaos(flow).setSession(request.getFlowName(), request.getDateId(),
                    ctx.getSession());
            } catch (Throwable e) {
            }
        }
    }

    private FlowContext createChaosContext(ChaosFlowRequest request, Flow flow)
        throws ContextCreateException, StatusException, SessionException {
        Pair<FlowStatus, String> status = null;

        status = StatusManager.getForChaos(flow).getStatus(request.getFlowName(), request.getDateId());

        //            更新上下文
        Map<String, Object> session = SessionManager.getForChaos(flow).getSession(request.getFlowName(),
            request.getDateId());
        if (null == status) {
            status = Pair.of(FlowStatus.INIT, flow.startNode().name);
        }
        if (null == session) {
            session = new HashMap<>();
        }
        DefaultContext ctx = new DefaultContext(RunMode.CHAOS, request, flow, status, session);
        request.setFlowContext(ctx);
        return ctx;
    }

    private FlowContext createContext(FlowRequest<?> request, Flow flow)
        throws ContextCreateException, StatusException, SessionException {
        Pair<FlowStatus, String> status = null;

        status = StatusManager.get(flow).getStatus(request.getFlowName(), request.getDateId());
        //            更新上下文
        Map<String, Object> session = SessionManager.get(flow).getSession(request.getFlowName(), request.getDateId());
        if (null == status) {
            status = Pair.of(FlowStatus.INIT, flow.startNode().name);
        }
        if (null == session) {
            session = new HashMap<>();
        }
        return new DefaultContext(RunMode.STABLE, request, flow, status, session);
    }

    public <T> void pause(String flowName, Serializable dateId) {
        Flow flow = FlowFactory.get(flowName);
        StatusManager.get(flow).pause(flowName, dateId);
    }

    public <T> void recover(String flowName, Serializable dateId) {
        Flow flow = FlowFactory.get(flowName);
        StatusManager.get(flow).recover(flowName, dateId);
    }

    public <T> void cancel(String flowName, Serializable dateId) {
        Flow flow = FlowFactory.get(flowName);
        StatusManager.get(flow).cancel(flowName, dateId);
    }
}
