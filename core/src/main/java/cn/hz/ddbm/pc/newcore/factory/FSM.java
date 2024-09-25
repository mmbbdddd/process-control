package cn.hz.ddbm.pc.newcore.factory;


import cn.hz.ddbm.pc.common.lang.Triple;
import cn.hz.ddbm.pc.newcore.FlowAttrs;
import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.StateAttrs;
import cn.hz.ddbm.pc.newcore.config.JvmProperties;
import cn.hz.ddbm.pc.newcore.fsm.FsmAction;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlow;
import cn.hz.ddbm.pc.newcore.fsm.Router;
import cn.hz.ddbm.pc.newcore.fsm.actions.LocalFsmAction;
import cn.hz.ddbm.pc.newcore.fsm.actions.RemoteFsmAction;

import java.util.List;
import java.util.Map;

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

    FlowAttrs flowAttrs();

    Map<String, StateAttrs> stateAttrs();

    /**
     * 定义插件，在每个节点执行前后执行。
     * 常用的插件有日志插件，监控埋点插件……
     *
     * @return
     */
    List<Plugin> plugins();

    /**
     * 流程变迁设置，包含三种类型
     * 事务业务：saga
     * 非事务业务：to
     * 查询业务：router
     *
     * @param
     */
    List<Triple<S, Class<? extends FsmAction>, Router<S>>> transitions();


    default FsmFlow build() throws Exception {
        S       init = initState();
        S       su   = suState();
        S       fail = failState();
        FsmFlow flow = new FsmFlow(flowId(), init, su, fail);

        List<Triple<S, Class<? extends FsmAction>, Router<S>>> events = transitions();
        events.forEach(t -> {
            if (LocalFsmAction.class.isAssignableFrom(t.getMiddle())) {
                flow.local(t.getLeft(), (Class<? extends LocalFsmAction>) t.getMiddle(), t.getRight());
            } else {
                flow.remote(t.getLeft(), (Class<? extends RemoteFsmAction>) t.getMiddle(), t.getRight());
            }
        });
        JvmProperties.flowAttrs.put(flowId(), flowAttrs());
        JvmProperties.flowAttrs.get(flowId()).setStateAttrs(stateAttrs());
        JvmProperties.flowAttrs.get(flowId()).setPlugins(plugins());

        return flow;
    }

}