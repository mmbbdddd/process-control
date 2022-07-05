package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.TimeoutException;
import io.ddbm.pc.utils.TimeoutWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

public class Pc implements ApplicationContextAware, ApplicationListener<Pc.FlowEvent> {
    Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationContext app;

    public FlowContext execute(String flowName, FlowRequest request, String event) throws PauseException {
        Assert.notNull(flowName, "flowName is null");
        Flow flow = Flows.get(flowName);
        Assert.notNull(flow, "flow[" + flowName + "] not exist");
        return flow.execute(request, event);
    }

    /**
     * 一次请求，跑到结束或者异常 ，并发挥异常
     */
    public void sync(String flowName, FlowRequest request, String event, TimeoutWatch timeout) throws InterruptException, TimeoutException {
        try {
            FlowContext ctx = execute(flowName, request, event);
            if (!ctx.syncIsStop(logger) && !timeout.isTimeout()) {
                sync(flowName, request, Coast.DEFAULT_EVENT, timeout);
            } else if (timeout.isTimeout()) {
                throw new TimeoutException();
            }
        } catch (PauseException e) {
            if (!e.getCtx().syncIsStop(logger) && !timeout.isTimeout()) {
                sync(flowName, request, Coast.DEFAULT_EVENT, timeout);
            } else if (timeout.isTimeout()) {
                throw new TimeoutException();
            }
        }
    }

    /**
     * 一次请求，跑到结束或者异常，首节点返回。其他节点异步。
     */
    public void async(String flowName, FlowRequest request, String event) {
        try {
            FlowContext ctx = execute(flowName, request, event);
            if (!ctx.asyncIsStop(logger)) {
                app.publishEvent(new FlowEvent(flowName, request, ctx));
            }
        } catch (PauseException e) {
            if (!e.getCtx().asyncIsStop(logger)) {
                app.publishEvent(new FlowEvent(flowName, request, e.getCtx()));
            }
        }
    }


    @Override
    public void onApplicationEvent(FlowEvent fe) {
        try {
            async(fe.flowName, fe.request, Coast.DEFAULT_EVENT);
        } catch (InterruptException e) {
            fe.ctx.setInterrupt(true, e);
        }
    }


    public void chaos(String flowName, FlowRequest request, String event) {
        Assert.notNull(flowName, "flowName is null");
        Flow flow = Flows.get(flowName);
        Assert.notNull(flow, "flow[" + flowName + "] not exist");
        injectMockAction(flow);
        try {
            execute(flowName, request, event);
            if (!flow.chaosIsStop(request.getStatus(), request.getSession(), event)) {
                chaos(flowName, request, Coast.DEFAULT_EVENT);
            }
        } catch (PauseException e) {
            if (!flow.chaosIsStop(request.getStatus(), request.getSession(), event)) {
                chaos(flowName, request, Coast.DEFAULT_EVENT);
            }
        }
    }

    private void injectMockAction(Flow flow) {
        flow.getNodes().forEach((nn, node) -> {
            node.events.forEach((en, event) -> {
                List<Action> action = Arrays.asList(new Action() {
                    //                    构建混沌action
                    @Override
                    public void execute(FlowContext ctx) throws PauseException, InterruptException {
                        Double d = Math.random();
                        if (d < 0.1) {
                            throw new PauseException(ctx, "随机出个暂停异常");
                        } else if (d < 0.2) {
                            throw new InterruptException("随机出个中断异常", ctx.getRequest().getStatus());
                        } else if (d < 0.3) {
                            throw new RuntimeException("随机出个异常");
                        } else if (d < 0.4) {
                            ctx.getRequest().setStatus("_randomNode");
                        } else {
//                        随机出个目标节点
                            String chaoNode = ctx.chaosNode();
                            ctx.getRequest().setStatus(chaoNode);
                        }
                    }
                });
                event.action.setAction(action);
            });
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.app = applicationContext;
    }


    protected class FlowEvent extends ApplicationEvent {
        private final String      flowName;
        private final FlowRequest request;
        private final FlowContext ctx;

        public FlowEvent(String flowName, FlowRequest request, FlowContext ctx) {
            super(request);
            this.flowName = flowName;
            this.request  = request;
            this.ctx      = ctx;
        }
    }

}
