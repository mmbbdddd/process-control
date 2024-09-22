package cn.hz.ddbm.pc.newcore.fsm.actions;


import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;

/**
 * @param <S>
 */
public class RemoteFsmActionProxy {
    Class<? extends RemoteFsmAction> actionType;

    public RemoteFsmActionProxy(Class<? extends RemoteFsmAction> actionType) {
        this.actionType = actionType;
    }


    public void doRemoteFsm(FlowContext<FsmState> ctx) {
        getAction().remoteFsm(ctx);
    }

    public Object remoteFsmQuery(FlowContext<FsmState> ctx) {
        return getAction().remoteFsmQuery(ctx);
    }
    RemoteFsmAction getAction() {
        return (RemoteFsmAction) ProcessorService.getAction(actionType);
    }
}
