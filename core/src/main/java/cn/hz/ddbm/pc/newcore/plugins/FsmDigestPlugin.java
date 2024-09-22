package cn.hz.ddbm.pc.newcore.plugins;

import cn.hz.ddbm.pc.common.lang.Triple;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.FlowStatus;
import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.State;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.log.Logs;

import java.io.Serializable;

public class FsmDigestPlugin extends Plugin<FsmState> {
    @Override
    public String code() {
        return "digest";
    }

    @Override
    public void preAction(FlowContext<FsmState> ctx) {

    }

    @Override
    public void postAction(FsmState preState, FlowContext<FsmState> ctx) {
        String       flow   = ctx.getFlow().name();
        Serializable id     = ctx.getId();
        FsmState     target = ctx.getState();

        Logs.digest.info("{},{},{}==>{}", flow, id, preState, target);
    }

    @Override
    public void errorAction(FsmState preState, Exception e, FlowContext<FsmState> ctx) {

    }

    @Override
    public void finallyAction(FsmState preNode, FlowContext<FsmState> ctx) {

    }


}
