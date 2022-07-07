package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.TimeoutException;
import io.ddbm.pc.support.ChaosAction;
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
//        Coast.setChaosMode(true);
        Assert.notNull(flowName, "flowName is null");
        Flow flow = Flows.get(flowName);
        Assert.notNull(flow, "flow[" + flowName + "] not exist");
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
