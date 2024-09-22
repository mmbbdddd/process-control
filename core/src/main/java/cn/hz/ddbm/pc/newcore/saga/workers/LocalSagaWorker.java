package cn.hz.ddbm.pc.newcore.saga.workers;


import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.exception.LimtedRetryException;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaActionProxy;

import static cn.hz.ddbm.pc.newcore.saga.SagaWorker.Offset.*;

public class LocalSagaWorker extends SagaWorker {
    LocalSagaActionProxy action;

    public LocalSagaWorker(Integer index, Class<? extends SagaAction> actionType) {
        super(index);
        this.action = new LocalSagaActionProxy(actionType);
    }

    /**
     * task.on(push.su)=>index++/task
     * task.on(push.fail)==>rollback;
     * task.on(push.exception)==>keep it;
     * task.on(push.exception && retrytime >? )==>manual;
     *
     * @param ctx
     */
    @Override
    public void execute(FlowContext<SagaState> ctx) {
        switch (ctx.state.offset) {
            case task: {
                Integer executeTimes = ctx.getExecuteTimes();
                Integer retryTimes   = ctx.getFlow().stateAttrs(ctx.getState()).getRetry();
                if (executeTimes > retryTimes) {
                    throw new LimtedRetryException();
                }
                SagaAction.QueryResult result = action.doLocalSaga(ctx);
                ctx.metricsState();
                onQueryResult(ctx, result);
                //todo 异常是否继续?
                break;
            }
            case rollback:
                Integer executeTimes = ctx.getExecuteTimes();
                Integer retryTimes = ctx.getFlow().stateAttrs(ctx.getState()).getRetry();
                if (executeTimes > retryTimes) {
                    throw new LimtedRetryException();
                }
                SagaAction.QueryResult result = action.doLocalSagaRollback(ctx);
                ctx.metricsState();
                onQueryResult(ctx, result);
                break;
        }

    }

    private void onQueryResult(FlowContext<SagaState> ctx, SagaAction.QueryResult result) {
        if (ctx.state.offset == task) {
            switch (result) {
                case exception:
                    ctx.state.offset = task;
                    break;
                case su:
                    ctx.state.index++;
                    ctx.state.offset = task;
                    break;
                case fail:
                    ctx.state.offset = rollback;
                    break;
            }
        } else if (ctx.state.offset == rollback) {
            switch (result) {
                case exception:
                    ctx.state.offset = rollback;
                    break;
                case su:
                    ctx.state.index--;
                    ctx.state.offset = rollback;
                    break;
                case fail:
                    ctx.state.flowStatus = FlowStatus.MANUAL;
                    break;
            }
        }
    }


}
