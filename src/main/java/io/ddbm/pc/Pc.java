package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;
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
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class Pc implements ApplicationContextAware, ApplicationListener<Pc.FlowEvent> {
    Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationContext app;

    public void execute(String flowName, FlowRequest request, String event, FlowResultListener listener) throws Exception {
        Assert.notNull(flowName, "flowName is null");
        Flow flow = Flows.get(flowName);
        Assert.notNull(flow, "flow[" + flowName + "] not exist");
        flow.execute(request, event, listener);
    }

    /**
     * 一次请求，跑到结束或者异常 ，并发挥异常
     */
    public FlowContext sync(String flowName, FlowRequest request, String event, TimeoutWatch timeout) throws InterruptException, TimeoutException {
        AtomicReference<FlowContext> holder = new AtomicReference<>(null);
        try {
            execute(flowName, request, event, new FlowResultListener() {
                @Override
                public void onResult(FlowContext ctx) throws Exception {
                    if (!ctx.isStop(logger) && !timeout.isTimeout()) {
                        sync(flowName, request, event, timeout);
                    } else {
                        holder.set(ctx);
                    }
                }

                @Override
                public void onPauseException(PauseException e) throws Exception {
                    if (!e.getCtx().isStop(logger) && !timeout.isTimeout()) {
                        sync(flowName, request, event, timeout);
                    } else {
                        holder.set(e.getCtx());
                    }
                }

                @Override
                public void onInterruptException(InterruptException e) throws Exception {
                    throw e;
                }
            });
        } catch (Exception e) {
            if (e instanceof InterruptException) {
                throw (InterruptException) e;
            } else if (e instanceof TimeoutException) {
                throw (TimeoutException) e;
            }
        }
        return holder.get();
    }

    /**
     * 一次请求，跑到结束或者异常，首节点返回。其他节点异步。
     */
    public FlowContext async(String flowName, FlowRequest request, String event) throws InterruptException {
        AtomicReference<FlowContext> holder = new AtomicReference<>(null);
        try {
            execute(flowName, request, event, new FlowResultListener() {
                @Override
                public void onResult(FlowContext ctx) throws Exception {
                    if (!ctx.isStop(logger)) {
                        app.publishEvent(new FlowEvent(flowName, request, ctx));
                    } else {
                        holder.set(ctx);
                    }
                }

                @Override
                public void onPauseException(PauseException e) throws Exception {
                    if (!e.getCtx().isStop(logger)) {
                        app.publishEvent(new FlowEvent(flowName, request, e.getCtx()));
                    } else {
                        holder.set(e.getCtx());
                    }
                }

                @Override
                public void onInterruptException(InterruptException e) throws Exception {
                    throw e;
                }
            });
        } catch (Exception e) {
            throw (InterruptException) e;
        }
        return holder.get();
    }


    @Override
    public void onApplicationEvent(FlowEvent fe) {
        try {
            async(fe.flowName, fe.request, Coast.DEFAULT_EVENT);
        } catch (InterruptException e) {
            fe.ctx.setInterrupt(true, e);
        }
    }


    public FlowContext test(String flowName, FlowRequest request, String event) throws InterruptException {
        Assert.notNull(flowName, "flowName is null");
        Flow flow = Flows.get(flowName);
        Assert.notNull(flow, "flow[" + flowName + "] not exist");
        injectMockAction(flow);
        AtomicReference<FlowContext> holder = new AtomicReference<>(null);
        try {
            execute(flowName, request, event, new FlowResultListener() {
                @Override
                public void onResult(FlowContext ctx) throws Exception {
                    holder.set(ctx);
                }

                @Override
                public void onPauseException(PauseException e) throws Exception {
                    holder.set(e.getCtx());
                }

                @Override
                public void onInterruptException(InterruptException e) throws Exception {
                    throw e;
                }
            });
        } catch (Exception e) {
            throw (InterruptException) e;
        }
        return holder.get();
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

    public FlowContext chaos(String flowName, FlowRequest request, String event) throws InterruptException {
        FlowContext ctx = test(flowName, request, event);
        if (!ctx.isStop()) {
            try {
                chaos(flowName, request, Coast.DEFAULT_EVENT);
            } catch (InterruptException e) {
                ctx.setInterrupt(true, e);
            }
        }
        return ctx;
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

    interface FlowResultListener {
        void onResult(FlowContext ctx) throws Exception;

        void onPauseException(PauseException e) throws Exception;

        void onInterruptException(InterruptException e) throws Exception;
    }
}
