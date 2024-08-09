package cn.hz.ddbm.pc.factory.buider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component
@WithStateMachine(name = "StateMachineConfig")
public class EventListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @OnTransition(target = "UNPAID")
    public boolean create(Message<Order> order) {
  // 创建订单逻辑
        logger.info("订单创建，待支付");
        return true;
    }

    @OnTransition(source = "UNPAID", target = "WAITING_FOR_RECEIVE")
    public boolean pay(Message<Order> order) throws Exception {
        // 支付逻辑 从redis根据order 来进行处理
        logger.info("用户完成支付，待收货");
        throw  new Exception("xxxx");

    }

    @OnTransition(source = "WAITING_FOR_RECEIVE", target = "DONE")
    public boolean receive(Message<Order> order) {
 //从redis中根据传入的order 来查询当前订单 并业务处理
        logger.info("用户已收货，订单完成");
        return true;
    }

}