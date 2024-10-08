package cn.hz.ddbm.pc.newcore.fsm;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import cn.hz.ddbm.pc.newcore.BaseFlow;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.exception.FlowEndException;
import cn.hz.ddbm.pc.newcore.exception.FlowStatusException;
import cn.hz.ddbm.pc.newcore.exception.LimtedRetryException;
import cn.hz.ddbm.pc.newcore.fsm.actions.LocalFsmAction;
import cn.hz.ddbm.pc.newcore.fsm.actions.RemoteFsmAction;
import cn.hz.ddbm.pc.newcore.log.Logs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FsmFlow implements BaseFlow<FsmState> {
    Map<Enum, FsmWorker> eventTables;
    String               name;
    Enum                 init;
    Enum                 su;
    Enum                 fail;

    public FsmFlow(String flowName, Enum init, Enum su, Enum fail) {
        this.name        = flowName;
        this.init        = init;
        this.su          = su;
        this.fail        = fail;
        this.eventTables = new HashMap<>();
    }

    @Override
    public void validate() {
        Assert.notNull(this.name, "name is null");
        Assert.notNull(this.init, "init is null");
        Assert.notNull(this.su, "su is null");
        Assert.notNull(this.fail, "fail is null");
        Assert.isTrue(!this.eventTables.isEmpty(), "eventTables is null");
    }

    @Override
    public String name() {
        return name;
    }

    public void execute(FlowContext<FsmState> ctx) throws FlowEndException, FlowStatusException {
        Assert.notNull(ctx, "ctx is null");
        Assert.notNull(ctx.state, "ctx.state is null");
        Assert.notNull(ctx.state.flowStatus, "ctx.flowstatus is null");
        Assert.notNull(ctx.state.state, "ctx.state is null");
        Assert.notNull(ctx.state.offset, "ctx.offset is null");
        Logs.flow.info("{},{}",ctx.id, ctx.state.state);
        if (isFail(ctx.state.state)) {
            ctx.state.flowStatus = (FlowStatus.FAIL);
            throw new FlowEndException();
        }
        if (isSu(ctx.state.state)) {
            ctx.state.flowStatus = (FlowStatus.SU);
            throw new FlowEndException();
        }
        FsmWorker worker = getWorker(ctx.state.state);
        if (!ctx.state.flowStatus.equals(FlowStatus.RUNNABLE)) {
            throw new FlowStatusException();
        }
        Integer executeTimes = ctx.getExecuteTimes();
        Integer retryTimes   = ctx.flow.stateAttrs(ctx.getState()).getRetry();
        if (executeTimes > retryTimes) {
            ctx.state.flowStatus = FlowStatus.INTERRUPTED;
            throw new LimtedRetryException();
        }
        worker.execute(ctx);
    }

    @Override
    public boolean isRunnable(FlowContext<FsmState> ctx) {
        return !isFail(ctx.state.state) && !isSu(ctx.state.state) && ctx.state.flowStatus.equals(FlowStatus.RUNNABLE);
    }


    private boolean isSu(Enum state) {
        return Objects.equals(su, state);
    }

    private boolean isFail(Enum state) {
        return Objects.equals(fail, state);
    }

    private FsmWorker getWorker(Enum state) {
        return eventTables.get(state);
    }

    public FsmFlow local(Enum from,   Class<? extends LocalFsmAction> action, Router router) {
        FsmWorker sagaFsmWorker = FsmWorker.local(this, from, action, router);
        this.eventTables.put(from,  sagaFsmWorker);
        return this;
    }

    public FsmFlow remote(Enum from, Class<? extends RemoteFsmAction> action, Router router) {
        FsmWorker sagaFsmWorker = FsmWorker.remote(this, from, action, router);
        this.eventTables.put(from, sagaFsmWorker);
        return this;
    }


}
