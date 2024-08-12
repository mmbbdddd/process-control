package cn.hz.ddbm.pc.factory.buider.pc;

import cn.hz.ddbm.pc.core.ActionAttrs;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.router.SagaRouter;
import cn.hz.ddbm.pc.factory.buider.FSM;
import cn.hz.ddbm.pc.factory.buider.StateMachine;
import cn.hz.ddbm.pc.factory.buider.StateMachineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class PcConfig implements FSM<PcConfig.PcState> {
    Logger logger = LoggerFactory.getLogger(getClass());

    public enum PcState {
        init("初始化"), sended("已发送"), send_failover("发送错误"), miss_data("客户资料缺乏"), miss_data_fulled("客户资料已补"), su("成功"), fail("失败"), error("异常");

        private final String descr;

        PcState(String descr) {
            this.descr = descr;
        }
    }

    public StateMachine<PcState> build(BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<PcState> builder = StateMachineBuilder.builder();
        logger.info("构建订单状态机");
//
        builder.withConfiguration()
                .machineId(machineid())
                .beanFactory(beanFactory);

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
                .register("sendRouter", new SagaRouter("send_failover", new HashMap<>()))
                .register("notifyRouter", new SagaRouter("miss_data", new HashMap<>()))
        ;

        return builder.build();
    }

    @Override
    public List<String> plugins() {
        return null;
    }

    @Override
    public String sessionManager() {
        return null;
    }

    @Override
    public String statusManager() {
        return null;
    }

    public String machineid() {
        return "test";
    }

}
