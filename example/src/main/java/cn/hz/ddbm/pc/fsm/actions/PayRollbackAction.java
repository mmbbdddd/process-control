package cn.hz.ddbm.pc.fsm.actions;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlow;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.FsmWorker;
import cn.hz.ddbm.pc.newcore.fsm.actions.RemoteFsmAction;
import cn.hz.ddbm.pc.saga.PayState;
import org.springframework.stereotype.Component;

@Component
public class PayRollbackAction implements RemoteFsmAction {


    @Override
    public void remoteFsm(FlowContext<FsmState> ctx) throws Exception {

    }

    @Override
    public Object remoteFsmQuery(FlowContext<FsmState> ctx) throws Exception {
        return null;
    }
}
