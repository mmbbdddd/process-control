package cn.hz.ddbm.pc.newcore.saga;


import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.newcore.*;
import cn.hz.ddbm.pc.newcore.saga.workers.FailWorker;
import cn.hz.ddbm.pc.newcore.saga.workers.SuWorker;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SagaFlow implements BaseFlow<SagaState> {
    List<SagaWorker> pipelines;
    FailWorker       failWorker;
    SuWorker         suWorker;
    String           name;

    public static SagaFlow of(String name,Class<? extends SagaAction>... actions) {
        List<SagaWorker> workers = new ArrayList<>();

        workers.add(SagaWorker.failWorker());
        for (int i = 0; i < actions.length; i++) {
            workers.add(SagaWorker.of(i, actions[i]));
        }
        workers.add(SagaWorker.suWorker(actions.length));
        return new SagaFlow(name,workers);
    }

    private SagaFlow(String name, List<SagaWorker> workers) {
        this.name       = name;
        this.pipelines  = workers;
        this.failWorker = (FailWorker) workers.get(0);
        this.suWorker   = (SuWorker) workers.get(workers.size() - 1);
    }

    @Override
    public String name() {
        return name;
    }

    public void execute(FlowContext<SagaState> ctx) {
        Assert.notNull(ctx, "ctx is null");
        Assert.notNull(ctx.state.index, "ctx.index is null");
        Assert.notNull(ctx.state.offset, "ctx.offset is null");
        log.info("{}", ctx.state.index);
        SagaWorker worker = getWorker(ctx);
        if (isFail(ctx.state.index)) {
            ctx.state.setFlowStatus(FlowStatus.FAIL);
            return;
        }
        if (isSu(ctx.state.index)) {
            ctx.state.setFlowStatus(FlowStatus.SU);
            return;
        }
        if (!ctx.state.getFlowStatus().equals(FlowStatus.RUNNABLE)) {
            return;
        }
        worker.execute(ctx);

        if (ctx.getFluent()) {
            execute(ctx);
        }
    }

    private boolean isSu(Integer index) {
        return Objects.equals(suWorker.index, index);
    }

    private boolean isFail(Integer index) {
        return Objects.equals(failWorker.index, index);
    }

    @Override
    public boolean isRunnable(FlowContext<SagaState> ctx) {
        return !isFail(ctx.state.index) && !isSu(ctx.state.index) && ctx.state.flowStatus.equals(FlowStatus.RUNNABLE);
    }


    private SagaWorker getWorker(FlowContext<SagaState> ctx) {
        return pipelines.stream().filter(w -> w.index.equals(ctx.state.getIndex())).findFirst().get();
    }


}