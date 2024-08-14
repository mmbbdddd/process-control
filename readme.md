# 流程编排

pc ：轻量级、高性能、高可靠的流程编排框架。

流程编排：一种关注点分离的架构组件。参见[功能：关注点分离](#功能)

# 问题

核心业务，流程长，数据一致性要求高、健壮性要求高、需求变化又比较频繁。

1，如何保证复杂长流程业务的一致性

2，如何保证复杂长流程业务的鲁棒性

3，如何保证复杂长流程业务的灵活性

在架构上，存在不存在一种解决方案，能同时满足这三者？

# 分析

解决这种需求的技术，就是所谓的流程编排。

    参见下图：流程编排解决了某一类的流程类架构需求

![img_6.png](doc/img_6.png)

      备注
      liteflow，ssm，cola等流程编排技术更接近于状态机——除了具备5（流程构建器）。流程编排的核心能力分布式任务重试、
      调度机制管理、状态管理、一致性……等都依赖应用自身去实现。不算是完全的流程编排。
      
      netflix的Maestro/Conductor则比较重量级，偏重于微服务编排。而不是流程步骤编排。 性能会比较差，但灵活性会比较强。
      介于工作流和流程编排之间。

# quickstart

![doc/img_16.png](doc/img_16.png)
**代码**

        <dependency>
            <groupId>cn.hz.ddbm.pc</groupId>
            <artifactId>pc</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

参见hz.ddbm.jfsm.pay包

 ```java
package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.factory.dsl.StateMachineBuilder;
import cn.hz.ddbm.pc.factory.dsl.StateMachineConfig;
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
            .initial(PcState.init)
            .ends(PcState.su, PcState.fail, PcState.error)
            .states(EnumSet.allOf(PcState.class))
    ;

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
            .register(new ExpressionRouter("sendRouter",
                    //sendRouter 有1/10的机会命中
                    new ExpressionRouter.NodeExpression("sendRouter", "Math.random() < 0.1"),
                    //su 有6/10的机会命中
                    new ExpressionRouter.NodeExpression("su", "Math.random() < 0.6"),
                    //fail 有1/10的机会命中
                    new ExpressionRouter.NodeExpression("fail", "Math.random() < 0.1"),
                    //error 有2/10的机会命中
                    new ExpressionRouter.NodeExpression("error", "Math.random() < 0.2")
            ))
            .register(new ExpressionRouter("notifyRouter",
                    new ExpressionRouter.NodeExpression("notifyRouter", "Math.random() <0.1"),
                    new ExpressionRouter.NodeExpression("su", "Math.random() < 0.6"),
                    new ExpressionRouter.NodeExpression("fail", "Math.random() < 0.1"),
                    new ExpressionRouter.NodeExpression("error", "Math.random() < 0.2")
            ))
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


@Test
public void pc() throws Exception {
  AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
  ctx.register(PcDemo.class);
  ctx.refresh();
  PcConfig pcConfig = new PcConfig();
  Flow     flow     = pcConfig.build(null);
  String   event    = Coasts.EVENT_DEFAULT;
  chaosService.addFlow(flow);

  try {
    //验证100次，统计流程健壮性报表
    chaosService.execute("test", new PayloadMock(flow.getInit().getName()), event, 100, 10);
  } catch (Exception e) {
    e.printStackTrace();
  }
}

 ```

**测试**

      19:12:32.001 [main] INFO cn.hz.ddbm.pc.example.PcConfig - 构建订单状态机
      19:12:32.064 [pool-1-thread-10] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.063 [pool-1-thread-13] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.063 [pool-1-thread-8] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.064 [pool-1-thread-20] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.065 [pool-1-thread-9] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.064 [pool-1-thread-16] INFO flow - 流程已限流：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,init,20>1      
      ..............
      19:12:32.069 [pool-1-thread-15] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.069 [pool-1-thread-10] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.069 [pool-1-thread-14] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-4] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-2] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-20] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-7] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-18] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-19] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-1] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-12] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.070 [pool-1-thread-8] INFO flow - 流程已结束：test,063a4fbc-19ca-415d-a0a5-5b76e027fbcd,su
      19:12:32.072 [main] INFO flow - 混沌测试报告：\n
      19:12:32.072 [main] INFO flow - FlowContext,PAUSE:sendRouter,1
      19:12:32.072 [main] INFO flow - FlowContext,RUNNABLE:sendRouter,4
      19:12:32.072 [main] INFO flow - FlowContext,PAUSE:init,18
      19:12:32.072 [main] INFO flow - FlowContext,RUNNABLE:init,5
      19:12:32.072 [main] INFO flow - FlowContext,RUNNABLE:fail,1
      19:12:32.072 [main] INFO flow - FlowContext,RUNNABLE:su,70

# 功能

* 1，积木化功能开发
* 2，技术和业务分离
    * 日志、监控埋点、指标埋点，事件通知 ……通过插件插入。
* 4，最终一致性（saga)
* 5，高性能（分布式事务所有实现中最高）
* 6，混沌
    * 通过混沌接口，模拟各种异常发生的概率，给出流程的鲁邦性报表和优化建议。
    * chaosAction，chaosRouter……不依赖于业务action实现。在设计阶段快速完成设计闭环。
* 7，关注点分离
    * 架构关注点：可读、可维护性、可扩展性、鲁棒性、高可用、一致性、高性能
    * 管理关注点：积木化、工程可管理性、风险可控
    * 开发关注点：明确的功能和边界，依赖，输出
    * 运维关注点：监控、日志
    * 大数据关注点：埋点、ab
    * 数字化运营关注点：指标，ROI，步骤环节、动态运营

# 架构介绍

![img.png](doc/img13.png)

# 能力词典

![img.png](doc/img.png)



 

 



 





