package cn.hz.ddbm.pc.factory.buider;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    public DemoApplication(StateMachine<OrderStateMachineConfig.OrderStatus, OrderStateMachineConfig.OrderEvent> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private final StateMachine<OrderStateMachineConfig.OrderStatus, OrderStateMachineConfig.OrderEvent> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        Order order=new Order("测试",0);
        // 使用 MessageBuilder 创建消息并设置负载和头信息
        Message message = MessageBuilder
                .withPayload(OrderStateMachineConfig.OrderEvent.PAYED)
                .setHeader("order", order)
                .build();
        // 发送消息给状态机
        stateMachine.start();
        stateMachine.sendEvent(message);
    }
}