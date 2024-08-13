package cn.hz.ddbm.pc.factory.buider.pc;

import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.SagaRouter;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.factory.buider.StateMachineBuilder;
import cn.hz.ddbm.pc.factory.buider.StateMachineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PcConfig implements StateMachineConfig<PcConfig.PcState> {
    Logger logger = LoggerFactory.getLogger(getClass());

    public enum PcState {
        init("初始化"), sended("已发送"), send_failover("发送错误"), miss_data("客户资料缺乏"), miss_data_fulled("客户资料已补"), su("成功"), fail("失败"), error("异常");

        private final String descr;

        PcState(String descr) {
            this.descr = descr;
        }
    }

    public Flow build(Container container) throws Exception {
        StateMachineBuilder.Builder<PcState> builder = StateMachineBuilder.builder(this);
        logger.info("构建订单状态机");

        builder.withStates()
                .initial("init")
                .ends(PcState.su, PcState.fail, PcState.error)
                .states(EnumSet.allOf(PcState.class));

        builder.withTransitions()
                .router(PcState.init, Coasts.EVENT_DEFAULT, "sendAction", "sendRouter", null)
                //发送异常，不明确是否发送
                .router(PcState.send_failover, Coasts.EVENT_DEFAULT, "sendQueryAction", "sendRouter", null)
                //已发送，对方处理中
                .router(PcState.sended, Coasts.EVENT_DEFAULT, "sendQueryAction", "sendRouter", null)
                //校验资料是否缺失&提醒用户  & ==》依然缺，已经补充
                .router(PcState.miss_data, Coasts.EVENT_DEFAULT, "validateAndNotifyUserAction", "notifyRouter", null)
//                资料就绪状态，可重新发送
                .to(PcState.miss_data_fulled, Coasts.EVENT_DEFAULT, "", PcState.init)
        //用户上传资料  && 更新资料状态
//                .to(PcState.miss_data, "uploade", "", "miss_data")
        ;

        builder.withRouters()
//                .register("simpleRouter", new ExpressionRouter(new HashMap<>()))
                .register(new SagaRouter("send_failover"))
                .register(new SagaRouter("miss_data"))
        ;

        return builder.build();
    }

    @Override
    public List<Plugin> plugins() {
        return new ArrayList<Plugin>();
    }

    @Override
    public SessionManager sessionManager() {
        return InfraUtils.getSessionManager(Coasts.SESSION_MANAGER_MEMORY);
    }

    @Override
    public StatusManager statusManager() {
        return InfraUtils.getStatusManager(Coasts.STATUS_MANAGER_MEMORY);
    }

    @Override
    public Map<String, Object> attrs() {
        return new HashMap<>();
    }


    public String machineId() {
        return "test";
    }

    @Override
    public String describe() {
        return "test";
    }

}
