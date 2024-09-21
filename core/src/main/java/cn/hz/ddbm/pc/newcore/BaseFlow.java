package cn.hz.ddbm.pc.newcore;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.config.JvmProperties;
import cn.hz.ddbm.pc.newcore.config.PcProperties;
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
    void execute(FlowContext<S> ctx) throws Exception;

    /**
     * 未结束，可运行
     *
     * @param ctx
     * @return
     */
    boolean isRunnable(FlowContext<S> ctx);

    /**
     * 获取流程属性定义
     *
     * @return
     */
    default FlowAttrs flowAttrs() {
        if (EnvUtils.isChaos()) {
            return chaosFlowAttrs();
        } else {
            FlowAttrs flowAttrs = getFlowAttrsByContainer();
            flowAttrs = null == flowAttrs ? getFlowAttrsByJvm() : defaultFlowAttrs();
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
            return chaosStateAttrs();
        } else {
            StateAttrs stateAttrs = getStateAttrsByContainer(state);
            stateAttrs = null == stateAttrs ? getStateAttrsByJvm(state) : defaultStateAttrs();
            return stateAttrs;
        }
    }

    default FlowAttrs getFlowAttrsByContainer() {
        PcProperties properties = SpringUtil.getBean(PcProperties.class);
        if (null == properties) return null;
        Map<String, FlowAttrs> flowAttrs = properties.getFlowAttrs();
        if (null == flowAttrs) return null;
        return flowAttrs.get(name());
    }

    default FlowAttrs getFlowAttrsByJvm() {
        Map<String, FlowAttrs> flowAttrs = JvmProperties.flowAttrs;
        if (null == flowAttrs) return null;
        return flowAttrs.get(name());
    }

    default StateAttrs getStateAttrsByContainer(S state) {
        FlowAttrs flowAttr = getFlowAttrsByContainer();
        if (null == flowAttr || flowAttr.getStateAttrs() == null) return null;
        return flowAttr.getStateAttrs().get(state.code());
    }

    default StateAttrs getStateAttrsByJvm(S state) {
        FlowAttrs flowAttr = getFlowAttrsByJvm();
        if (null == flowAttr || flowAttr.getStateAttrs() == null) return null;
        return flowAttr.getStateAttrs().get(state.code());
    }

    default FlowAttrs defaultFlowAttrs() {
        FlowAttrs f = new FlowAttrs();
        f.namespace     = "app";
        f.maxLoop       = 3;
        f.statusTimeout = 3000;
        f.lockTimeout   = 3000;
        f.status        = Coast.StatusType.redis;
        f.session       = Coast.SessionType.redis;
        f.lock          = Coast.LockType.redis;
        f.statistics    = Coast.StatisticsType.redis;
        f.schedule      = Coast.ScheduleType.timer;
        f.plugins       = new ArrayList<>();
        return f;
    }

    default FlowAttrs chaosFlowAttrs() {
        FlowAttrs f = new FlowAttrs();
        f.namespace     = "app";
        f.maxLoop       = 3;
        f.statusTimeout = 3000;
        f.lockTimeout   = 3000;
        f.status        = Coast.StatusType.jvm;
        f.session       = Coast.SessionType.jvm;
        f.lock          = Coast.LockType.jvm;
        f.statistics    = Coast.StatisticsType.jvm;
        f.schedule      = Coast.ScheduleType.timer;
        f.plugins       = new ArrayList<>();
        return f;
    }


    default StateAttrs defaultStateAttrs() {
        StateAttrs s = new StateAttrs();
        s.retry = 0;
        return s;
    }


    default StateAttrs chaosStateAttrs() {
        StateAttrs s = new StateAttrs();
        s.retry = 10;
        return s;
    }

}