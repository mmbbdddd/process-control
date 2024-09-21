package cn.hz.ddbm.pc.newcore.saga.workers;


import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import cn.hz.ddbm.pc.newcore.saga.actions.RemoteSagaAction;
import cn.hz.ddbm.pc.newcore.saga.actions.RemoteSagaActionProxy;

import static cn.hz.ddbm.pc.newcore.saga.SagaWorker.Offset.*;

public class RemoteSagaWorker extends SagaWorker {
    RemoteSagaActionProxy action;

    public RemoteSagaWorker(Integer index, Class<? extends SagaAction> actionType) {
        super(index);
        this.action = new RemoteSagaActionProxy(actionType);
    }

    @Override
    public void execute(FlowContext<SagaState> ctx) {
        switch (ctx.state.offset) {
            case task:
//                任务执行之前状态先设置为task_failover
                ctx.state.offset = task_query;
                action.doRemoteSaga(ctx);
                break;
            case task_query:
//                查询任务后递归执行状态机
                onQueryResult(ctx, action.remoteSagaQuery(ctx));
                //todo 异常是否继续.
                execute(ctx);
                break;
            case rollback:
//                任务执行之前状态先设置为rollback_failover
                ctx.state.offset = rollback_query;
                action.doRemoteSagaRollback(ctx);
                break;
            case rollback_query:
                onQueryResult(ctx, action.remoteSagaRollbackQuery(ctx));
                //todo 异常是否继续
                execute(ctx);
                break;
        }
    }

    private void onQueryResult(FlowContext<SagaState> ctx, RemoteSagaAction.QueryResult result) {
        if (ctx.state.offset == task || ctx.state.offset == task_query) {
            switch (result) {
                case none:
                    ctx.state.offset = task;
                    break;
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
                case none:
                    ctx.state.offset = rollback;
                    break;
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
