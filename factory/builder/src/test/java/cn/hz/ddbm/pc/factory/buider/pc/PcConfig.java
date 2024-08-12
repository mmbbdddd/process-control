package cn.hz.ddbm.pc.factory.buider.pc;

import cn.hz.ddbm.pc.factory.buider.FSM;
import cn.hz.ddbm.pc.factory.buider.StateMachine;
import cn.hz.ddbm.pc.factory.buider.StateMachineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.util.EnumSet;

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
                .initial("UNPAID")
                .states(EnumSet.allOf(PcState.class));

        builder.withTransitions()
                .toEvent("", "", "", "")
                .routerEvent("", "", "", "",null);

        return builder.build();
    }

    public String machineid() {
        return "test";
    }

}
