package cn.hz.ddbm.pc.newcore.factory;


import cn.hz.ddbm.pc.common.lang.Tetrad;
import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.fsm.FsmAction;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlow;
import cn.hz.ddbm.pc.newcore.fsm.Router;
import cn.hz.ddbm.pc.newcore.fsm.actions.LocalFsmAction;
import cn.hz.ddbm.pc.newcore.fsm.actions.RemoteFsmAction;

import java.util.List;

public interface FSM<S extends Enum<S>> {
    /**
     * 状态机ID
     *
     * @return
     */
    String flowId();

    /**
     * 状态机说明
     *
     * @return
     */
    String describe();

    /**
     * @return
     */
    S initState();

    /**
     * @return
     */
    S suState();

    /**
     * @return
     */
    S failState();

    /**
     * 定义插件，在每个节点执行前后执行。
     * 常用的插件有日志插件，监控埋点插件……
     *
     * @return
     */
    List<Plugin> plugins();

    /**
     * 参见profile
     *
     * @return
     */
    Coast.SessionType session();

    /**
     * 参见profile
     *
     * @return
     */
    Coast.StatusType status();


    /**
     * 流程变迁设置，包含三种类型
     * 事务业务：saga
     * 非事务业务：to
     * 查询业务：router
     *
     * @param
     */
    List<Tetrad<S, String, Class<? extends FsmAction>, Router<S>>> transitions();


    Class<S> type();


    default FsmFlow build() throws Exception {
        S       init = initState();
        S       su   = suState();
        S       fail = failState();
        FsmFlow flow = new FsmFlow(flowId(), init, su, fail);

        List<Tetrad<S, String, Class<? extends FsmAction>, Router<S>>> events = transitions();
        events.forEach(t -> {
            if (LocalFsmAction.class.isAssignableFrom(t.getThree())) {
                flow.local(t.getOne(), t.getTwo(), (Class<? extends LocalFsmAction>) t.getThree(), t.getFour());
            } else {
                flow.remote(t.getOne(), t.getTwo(), (Class<? extends RemoteFsmAction>) t.getThree(), t.getFour());
            }
        });
        return flow;
    }

}