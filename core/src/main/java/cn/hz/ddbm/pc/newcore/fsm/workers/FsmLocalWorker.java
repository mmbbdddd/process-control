package cn.hz.ddbm.pc.newcore.fsm.workers;


import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.config.ErrorCode;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlow;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.FsmWorker;
import cn.hz.ddbm.pc.newcore.fsm.Router;
import cn.hz.ddbm.pc.newcore.fsm.actions.LocalFsmAction;

public class FsmLocalWorker extends FsmWorker {
    Class<? extends LocalFsmAction> actionType;

    public FsmLocalWorker(FsmFlow fsm, Enum from, Class<? extends LocalFsmAction> actionType, Router router) {
        super(fsm, from, router);
        this.actionType = actionType;
    }

    @Override
    public void execute(FlowContext<FsmState> ctx)   {
        //如果任务可执行
        Object result = getAction().doLocalFsm(ctx);
        ctx.metricsState();
        Enum state = router.router(ctx, result);
        if (null == state) {
            //如果结果为空，路由无结果，1，暂停，2报错
            ctx.state.flowStatus = FlowStatus.MANUAL;
            ctx.errorMessage     = ErrorCode.ROUTER_RESULT_EMPTY;
        } else if (state.equals(ctx.getState().state)) {
            //如果状态不变，在告警（todo）告警，告警，告警，或者人工处理把。
            ctx.state.flowStatus = FlowStatus.MANUAL;
            ctx.errorMessage     = "节点执行后状态不变！！";
        } else {
            ctx.state.state  = state;
            ctx.state.offset = Offset.task;
        }
    }

    LocalFsmAction getAction() {
        return ProcessorService.getAction(actionType);
    }

}
