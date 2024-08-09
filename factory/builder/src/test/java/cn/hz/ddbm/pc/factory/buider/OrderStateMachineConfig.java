package cn.hz.ddbm.pc.factory.buider;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "StateMachineConfig")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStateMachineConfig.OrderStatus, OrderStateMachineConfig.OrderEvent> {
    /**
     * 配置状态
     *
     * @param states
     * @throws Exception
     */
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.WAIT_PAYMENT)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    /**
     * 配置状态转换事件关系
     *
     * @param transitions
     * @throws Exception
     */
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                //支付事件:待支付-》待发货
                .withExternal().source(OrderStatus.WAIT_PAYMENT).target(OrderStatus.WAIT_DELIVER).event(OrderEvent.PAYED)
                .and()
                //发货事件:待发货-》待收货
                .withExternal().source(OrderStatus.WAIT_DELIVER).target(OrderStatus.WAIT_RECEIVE).event(OrderEvent.DELIVERY)
                .and()
                //收货事件:待收货-》已完成
                .withExternal().source(OrderStatus.WAIT_RECEIVE).target(OrderStatus.FINISH).event(OrderEvent.RECEIVED);
    }

    public enum OrderStatus {
        // 待支付，待发货，待收货，已完成
        WAIT_PAYMENT(1, "待支付"),
        WAIT_DELIVER(2, "待发货"),
        WAIT_RECEIVE(3, "待收货"),
        FINISH(4, "已完成");
        private Integer key;
        private String  desc;

        OrderStatus(Integer key, String desc) {
            this.key  = key;
            this.desc = desc;
        }

        public Integer getKey() {
            return key;
        }

        public String getDesc() {
            return desc;
        }

        public static OrderStatus getByKey(Integer key) {
            for (OrderStatus e : values()) {
                if (e.getKey().equals(key)) {
                    return e;
                }
            }
            throw new RuntimeException("enum not exists.");
        }
    }

    public enum OrderEvent {
        // 支付，发货，确认收货
        PAYED, DELIVERY, RECEIVED;
    }

}
