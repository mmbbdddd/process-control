package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pc {
    Logger          logger = LoggerFactory.getLogger(getClass());
    //todo 待优化
    ExecutorService es     = Executors.newFixedThreadPool(3);

    public FlowContext execute(String flowName, FlowRequest request, String event) throws PauseException, InterruptException {
        Assert.notNull(flowName, "flowName is null");
        Flow flow = Flows.get(flowName);
        Assert.notNull(flow, "flow[" + flowName + "] not exist");
        return flow.execute(request, event);
    }

    public FlowContext fluent(String flowName, FlowRequest request, String event) throws PauseException, InterruptException {
        FlowContext ctx = null;
        try {
            ctx = execute(flowName, request, event);
        } finally {
            final FlowContext _ctx = ctx;
            if (null != _ctx && !_ctx.isPause(logger)) {
                es.submit(() -> {
                    try {
                        fluent(flowName, request, Coast.DEFAULT_EVENT);
                    } catch (PauseException | InterruptException e) {
                        _ctx.setInterrupt(true, e);
                    }
                });
            }
        }
        return ctx;
    }


    public FlowContext test(String flowName, FlowRequest request, String event) throws PauseException, InterruptException {
        Assert.notNull(flowName, "flowName is null");
        Flow flow = Flows.get(flowName);
        Assert.notNull(flow, "flow[" + flowName + "] not exist");
        injectMockAction(flow);
        return execute(flowName, request, event);
    }

    private void injectMockAction(Flow flow) {
        flow.getNodes().forEach((nn, node) -> {
            node.events.forEach((en, event) -> {
                List<Action> action = Arrays.asList(new Action() {
                    @Override
                    public void execute(FlowContext ctx) throws PauseException, InterruptException {
                        Double d = Math.random();
                        if (d < 0.1) {
                            throw new PauseException(ctx, "随机出个暂停异常");
                        } else if (d < 0.2) {
//                            throw new InterruptException("随机出个中断异常", ctx.getRequest().getStatus());
                        } else if (d < 0.3) {
                            throw new RuntimeException("随机出个异常");
                        }
                        String chaoNode = ctx.chaosNode();
                        ctx.getRequest().setStatus(chaoNode);
                    }
                });
                event.action.injectChaosAction(action);
            });
        });
    }

    public FlowContext chaos(String flowName, FlowRequest request, String event) throws InterruptException {
        FlowContext ctx = null;
        try {
            try {
                ctx = test(flowName, request, event);
            } catch (PauseException e) {
                ctx = e.getCtx();
            }
        } finally {
            if (null != ctx && !ctx.isPause()) {
                try {
                    chaos(flowName, request, Coast.DEFAULT_EVENT);
                } catch (InterruptException e) {
                    ctx.setInterrupt(true, e);
//                    logger.info("flow:{},id:{} interrupt ", flowName, request.getId());
                }
            }
        }
        return ctx;
    }

}
