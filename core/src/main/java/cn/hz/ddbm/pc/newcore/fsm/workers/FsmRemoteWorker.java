package cn.hz.ddbm.pc.newcore.fsm.workers;


import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.config.ErrorCode;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlow;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.FsmWorker;
import cn.hz.ddbm.pc.newcore.fsm.Router;
import cn.hz.ddbm.pc.newcore.fsm.actions.RemoteFsmAction;

import static cn.hz.ddbm.pc.newcore.fsm.FsmWorker.Offset.failover;

public class FsmRemoteWorker extends FsmWorker {
    Class<? extends RemoteFsmAction> actionType;

    public FsmRemoteWorker(FsmFlow fsm, Enum from, Class<? extends RemoteFsmAction> actionType, Router router) {
        super(fsm, from, router);
        this.actionType = actionType;
    }

    @Override
    public void execute(FlowContext<FsmState> ctx) {

        //如果任务可执行
        Offset offset = ctx.state.offset;
        switch (offset) {
            case task:
                ctx.state.offset = failover;
                getAction().remoteFsm(ctx);
                ctx.metricsState();
                break;
            case failover:
                Object result = getAction().remoteFsmQuery(ctx);
                ctx.metricsState();
                Enum state = router.router(ctx,result);
                if (null == state) {
                    ctx.state.flowStatus = FlowStatus.MANUAL;
                    ctx.errorMessage     = ErrorCode.ROUTER_RESULT_EMPTY;
                } else if(state.equals(ctx.getState().state)) {
                    ctx.state.flowStatus = FlowStatus.MANUAL;
                    ctx.errorMessage = "节点执行后状态不变";
                } else {
                    ctx.state.state  = state;
                    ctx.state.offset = Offset.task;
                }
                break;
        }
    }
    RemoteFsmAction getAction() {
        return ProcessorService.getAction(actionType);
    }
}
