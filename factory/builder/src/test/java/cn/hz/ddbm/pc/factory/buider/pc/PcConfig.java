package cn.hz.ddbm.pc.factory.buider.pc;

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
        init, sended, send_error, miss_data, miss_data_fulled, su, fail, error
    }

    public StateMachine<PcState, String, String, String> build(BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<PcState, String, String, String> builder = StateMachineBuilder.builder();
        logger.info("构建订单状态机");
//
        builder.withConfiguration()
                .machineId(machineid())
                .beanFactory(beanFactory);

        builder.withStates()
                .initial("init")
                .ends("su", "fail", "error")
                .states(EnumSet.allOf(PcState.class));

        builder.withTransitions()
                .to("", "", "", "")
                .router("", "", "", "", null);

        builder.withRouters()
                .register("simpleRouter", new ExpressionRouter(new HashMap<>()))
                .register("sagaRouter", new SagaRouter("a", new HashMap<>()));

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
