//package fsm;
//
//import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
//import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
//
//import java.util.EnumSet;
//
//
//public class OrderFsm extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {
//
//    /**
//     * 配置状态
//     *
//     * @param states
//     * @throws Exception
//     */
//    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states)
//        throws Exception {
//        states.withStates().initial(OrderStatus.INIT).states(EnumSet.allOf(OrderStatus.class));
//    }
//
//    /**
//     * 配置状态转换事件关系
//     *
//     * @param transitions
//     * @throws Exception
//     */
//    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions)
//        throws Exception {
//        transitions
//            //支付事件:待支付-》待发货
//            .withExternal().source(OrderStatus.INIT).target(OrderStatus.PAY_ED).event(OrderEvent.SUBMIT).and()
//            //发货事件:待发货-》待收货
//            .withExternal().source(OrderStatus.PAY_ED).target(OrderStatus.SU).event(
//                OrderEvent.SU).and().withExternal().source(OrderStatus.PAY_ED).target(OrderStatus.FAIL).event(
//                OrderEvent.FAIL).and().withExternal().source(OrderStatus.PAY_ED).target(OrderStatus.PAY_ERROR).event(
//                OrderEvent.EXCEPTION).and().withExternal().source(OrderStatus.PAY_ERROR).target(OrderStatus.SU).event(
//                OrderEvent.SU).and().withExternal().source(OrderStatus.PAY_ERROR).target(OrderStatus.FAIL).event(
//                OrderEvent.FAIL).and().withExternal().source(OrderStatus.PAY_ERROR).target(OrderStatus.PAY_ERROR)
//                .event(
//                OrderEvent.EXCEPTION);
//    }
//
//}
