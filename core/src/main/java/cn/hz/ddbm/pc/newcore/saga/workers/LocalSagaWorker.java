package cn.hz.ddbm.pc.newcore.saga.workers;


import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import cn.hz.ddbm.pc.newcore.saga.actions.LocalSagaActionProxy;

import static cn.hz.ddbm.pc.newcore.saga.SagaWorker.Offset.rollback;
import static cn.hz.ddbm.pc.newcore.saga.SagaWorker.Offset.task;

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
                try {
                    Boolean result = action.doLocalSaga(ctx);
                    if (result) {
                        ctx.state.index++;
                        ctx.state.offset = task;
                    } else {
                        ctx.state.offset = rollback;
                    }
                } catch (Exception e) {
                    //keep state is task
                    throw e;
                }
                break;
            }
            case rollback:
                try {
                    Boolean result = action.doLocalSagaRollback(ctx);
                    if (result) {
                        ctx.state.index--;
                        ctx.state.offset = rollback;
                    } else {
                        ctx.state.setFlowStatus(FlowStatus.MANUAL);
                    }
                } catch (Exception e) {
                    //keep state rollback until retry time > ? then set state manual
                    //todo 当执行次数超限的时候，回滚
                    ctx.state.setFlowStatus(FlowStatus.MANUAL);
                    throw e;
                }
                break;
        }

    }


}
