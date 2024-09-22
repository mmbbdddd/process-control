package cn.hz.ddbm.pc.newcore.plugins;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.log.Logs;
import cn.hz.ddbm.pc.newcore.saga.SagaState;

public class SagaDigestPlugin extends Plugin<SagaState> {
    @Override
    public String code() {
        return "digest";
    }

    @Override
    public void preAction(FlowContext<SagaState> ctx) {

    }

    @Override
    public void postAction(SagaState preState, FlowContext<SagaState> ctx) {
        String    flow   = ctx.getFlow().name();
        String    id     = ctx.getId();
        SagaState target = ctx.getState();
        Logs.digest.info(" {},{},{}==>{}", flow, id, preState, target);
    }

    @Override
    public void errorAction(SagaState preState, Exception e, FlowContext<SagaState> ctx) {

    }

    @Override
    public void finallyAction(SagaState preNode, FlowContext<SagaState> ctx) {

    }

//
//    @Override
//    public void preAction(FlowContext<?, SagaState<S>, ?> ctx) {
//
//    }
//
//    @Override
//    public void postAction(SagaState<S> lastNode, FlowContext<?, SagaState<S>, ?> ctx) {
//
//        String                                        flow   = ctx.getFlow().getName();
//        Serializable                                  id     = ctx.getId();
//        Triple<FlowStatus, Integer, SagaState.Offset> from   = lastNode.code();
//        String                                        action = ctx.getAction().code();
//        Triple<FlowStatus, Integer, SagaState.Offset> target = ctx.getState().code();
//        String fromStateStr = String.format("%s:%s:%s",from.getMiddle(),from.getRight(),from.getLeft());
//        String targetStateStr = String.format("%s:%s:%s",target.getMiddle(),target.getRight(),target.getLeft());
//        Logs.digest.info(" {},{}, {},{}==>{}", flow, id, action, fromStateStr, targetStateStr);
//
//    }
//
//    @Override
//    public void errorAction(SagaState<S> preNode, Exception e, FlowContext<?, SagaState<S>, ?> ctx) {
//
//    }
//
//    @Override
//    public void finallyAction(State preNode, FlowContext<?, SagaState<S>, ?> ctx) {
////        log.info("{},{},{},{}", ctx.getFlow().getName(), ctx.getId(),  ctx.getAction().code(), ctx.getActionResult());
////        log.info("{},{},{},{}", ctx.getFlow().getName(), ctx.getId(),  ctx.getStatus(), ctx.getState());
//    }


}
