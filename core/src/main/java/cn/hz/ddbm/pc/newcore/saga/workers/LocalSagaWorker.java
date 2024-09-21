package cn.hz.ddbm.pc.newcore.saga.workers;


import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaActionProxy;
import cn.hz.ddbm.pc.newcore.saga.actions.RemoteSagaAction;

import static cn.hz.ddbm.pc.newcore.saga.SagaWorker.Offset.*;
import static cn.hz.ddbm.pc.newcore.saga.SagaWorker.Offset.rollback_query;

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
                SagaAction.QueryResult result = action.doLocalSaga(ctx);
                onQueryResult(ctx, result);
                //todo 异常是否继续?
                execute(ctx);
                break;
            }
            case rollback:
                SagaAction.QueryResult result = action.doLocalSagaRollback(ctx);
                onQueryResult(ctx, result);
                //todo 异常是否继续?
                execute(ctx);
                break;
        }

    }

    private void onQueryResult(FlowContext<SagaState> ctx, SagaAction.QueryResult result) {
        if (ctx.state.offset == task || ctx.state.offset == task_query) {
            switch (result) {
                case exception:
                    ctx.state.offset = task_query;
                    break;
                case su:
                    ctx.state.index++;
                    ctx.state.offset = task;
                    break;
                case fail:
                    ctx.state.offset = rollback;
                    break;
            }
        } else if (ctx.state.offset == rollback || ctx.state.offset == rollback_query) {
            switch (result) {
                case exception:
                    ctx.state.offset = rollback_query;
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
