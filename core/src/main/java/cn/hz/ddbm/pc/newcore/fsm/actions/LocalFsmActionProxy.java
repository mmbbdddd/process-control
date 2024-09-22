package cn.hz.ddbm.pc.newcore.fsm.actions;


import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.saga.actions.RemoteSagaAction;
import org.springframework.context.ApplicationContext;

/**
 *
 */
public class LocalFsmActionProxy {
    Class<? extends LocalFsmAction> actionType;

    public LocalFsmActionProxy(Class<? extends LocalFsmAction> actionType) {
        this.actionType = actionType;
    }


    public <S extends Enum<S>> Object doLocalFsm(FlowContext<FsmState> ctx) throws Exception {
        //提交事务
        return getAction().doLocalFsm(ctx);
    }

    LocalFsmAction getAction() {
        return ProcessorService.getAction(actionType);
    }

    public String actioname() {
        return null;
    }
}
