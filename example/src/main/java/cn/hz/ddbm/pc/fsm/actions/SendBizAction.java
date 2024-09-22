package cn.hz.ddbm.pc.fsm.actions;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.actions.RemoteFsmAction;

public class SendBizAction implements RemoteFsmAction {

    @Override
    public void remoteFsm(FlowContext<FsmState> ctx)   {

    }

    @Override
    public Object remoteFsmQuery(FlowContext<FsmState> ctx)   {
        return null;
    }
}
