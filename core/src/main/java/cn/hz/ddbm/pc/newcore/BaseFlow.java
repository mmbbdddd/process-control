package cn.hz.ddbm.pc.newcore;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.config.JvmProperties;
import cn.hz.ddbm.pc.newcore.config.PcProperties;
import cn.hz.ddbm.pc.newcore.exception.FlowEndException;
import cn.hz.ddbm.pc.newcore.exception.FlowStatusException;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;

import java.util.ArrayList;
import java.util.Map;

public interface BaseFlow<S extends State> {
    /**
     * 流程名
     *
     * @return
     */
    String name();

    /**
     * 流程执行
     *
     * @param ctx
     * @throws Exception
     */
    void execute(FlowContext<S> ctx) throws FlowEndException, FlowStatusException;

    /**
     * 未结束，可运行
     *
     * @param ctx
     * @return
     */
    boolean isRunnable(FlowContext<S> ctx);

    void validate();

    /**
     * 获取流程属性定义
     *
     * @return
     */
    default FlowAttrs flowAttrs() {
        if (EnvUtils.isChaos()) {
            return FlowAttrs.chaosOf();
        } else {
            FlowAttrs flowAttrs = FlowHelper.getFlowAttrsByContainer(this);
            if (null == flowAttrs) {
                flowAttrs = FlowHelper.getFlowAttrsByJvm(this);
            }
            if (null == flowAttrs) {
                flowAttrs = FlowAttrs.defaultOf();
            }
            return flowAttrs;
        }
    }

    /**
     * 获取状态属性定义
     *
     * @param state
     * @return
     */
    default StateAttrs stateAttrs(S state) {
        if (EnvUtils.isChaos()) {
            return StateAttrs.ChaosOf();
        } else {
            StateAttrs stateAttrs = FlowHelper.getStateAttrsByContainer(this, state);
            if (stateAttrs == null) {
                stateAttrs = FlowHelper.getStateAttrsByJvm(this, state);
            }
            if (stateAttrs == null) {
                stateAttrs = StateAttrs.defaultOf();
            }
            return stateAttrs;
        }
    }
}

abstract class FlowHelper {

    static FlowAttrs getFlowAttrsByContainer(BaseFlow flow) {
        try {
            PcProperties properties = SpringUtil.getBean(PcProperties.class);
            if (null == properties) return null;
            Map<String, FlowAttrs> flowAttrs = properties.getFlowAttrs();
            if (null == flowAttrs) return null;
            return flowAttrs.get(flow.name());
        } catch (Exception e) {
            return null;
        }
    }

    static FlowAttrs getFlowAttrsByJvm(BaseFlow flow) {
        Map<String, FlowAttrs> flowAttrs = JvmProperties.flowAttrs;
        if (null == flowAttrs) return null;
        return flowAttrs.get(flow.name());
    }

    static <S extends State> StateAttrs getStateAttrsByContainer(BaseFlow flow, S state) {
        FlowAttrs flowAttr = getFlowAttrsByContainer(flow);
        if (null == flowAttr || flowAttr.getStateAttrs() == null) return null;
        return flowAttr.getStateAttrs().get(state.code());
    }

    static <S extends State> StateAttrs getStateAttrsByJvm(BaseFlow flow, S state) {
        FlowAttrs flowAttr = getFlowAttrsByJvm(flow);
        if (null == flowAttr || flowAttr.getStateAttrs() == null) return null;
        return flowAttr.getStateAttrs().get(state.code());
    }

}
