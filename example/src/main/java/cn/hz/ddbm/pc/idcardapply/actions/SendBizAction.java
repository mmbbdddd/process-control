package cn.hz.ddbm.pc.idcardapply.actions;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.actions.RemoteFsmAction;

public class SendBizAction implements RemoteFsmAction {

    @Override
    public void remoteFsm(FlowContext<FsmState> ctx) throws Exception {

    }

    @Override
    public Object remoteFsmQuery(FlowContext<FsmState> ctx) throws Exception {
        return null;
    }
}
