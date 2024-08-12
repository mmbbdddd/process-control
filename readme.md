# 流程编排

pc    ：轻量级、高性能、高可靠的流程编排框架。

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
      <groupId>hz.ddbm</groupId>
      <artifactId>pc</artifactId> 
      <version>${version}</version>
    </dependency>

参见hz.ddbm.jfsm.pay包

 ```java
 public class PayFsm extends Fsm {
    //    定义工作流
    @Override
    public Flow flow() {
        return new FlowBuilder("testFlow", null, null) {
            {
                addNode(new NodeBuilder(Node.Type.START, "init") {{
                    onEvent(Coast.EVENT_PUSH, "payAction", "any://payRouter", "pay_unknow", "true");
                }});
                addNode(new NodeBuilder(Node.Type.TASK, "pay_unknow") {{
                    onEvent(Coast.EVENT_PUSH, "payQueryAction", "any://payRouter");
                }});
                addNode(new NodeBuilder(Node.Type.END, "su"));
                addNode(new NodeBuilder(Node.Type.END, "fail"));

                addRouter(payRouter());
                addPlugin("logPlugin");
            }
//            回忆

        }.build();
    }

    //定义路由
    private Router payRouter() {
        return new Router.Any("payRouter", new HashMap<String, String>() {{
            put("su", "null != actionResult && actionResult.code == '0000'");
            put("fail", "null != actionResult &&  actionResult.code == '0001'");
            put("pay_unknow", "true");
        }});
    }

}
 ```

**测试**

     pcService.executeMore("testFlow",1, new Object(),Coast.EVENT_PUSH,null)


    2024-06-12 11:33:23.700  INFO 20140 --- [           main] hz.ddbm.pc.core.PcServiceTest            : Starting PcServiceTest using Java 1.8.0_381 on DESKTOP-RJKO1VK with PID 20140 (started by wanglin in D:\project\process-control)
    2024-06-12 11:33:23.700  INFO 20140 --- [           main] hz.ddbm.pc.core.PcServiceTest            : No active profile set, falling back to 1 default profile: "default"
    2024-06-12 11:33:24.096  INFO 20140 --- [           main] o.s.c.a.ConfigurationClassEnhancer       : @Bean method PcConfiguration.infraUtils is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. This will result in a failure to process annotations such as @Autowired, @Resource and @PostConstruct within the method's declaring @Configuration class. Add the 'static' modifier to this method to avoid these container lifecycle issues; see @Bean javadoc for complete details.
    2024-06-12 11:33:24.175  INFO 20140 --- [           main] hz.ddbm.pc.core.service.PcService                : 初始化流程:testFlow
    2024-06-12 11:33:24.698  INFO 20140 --- [           main] hz.ddbm.pc.core.PcServiceTest            : Started PcServiceTest in 1.204 seconds (JVM running for 1.852)
    2024-06-12 11:33:24.824  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,pay_unknow,pay_unknow
    2024-06-12 11:33:24.840  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,init,pay_unknow
    2024-06-12 11:33:24.840  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,pay_unknow,pay_unknow
    2024-06-12 11:33:24.840  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,pay_unknow,pay_unknow
    2024-06-12 11:33:24.840  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,pay_unknow,pay_unknow
    2024-06-12 11:33:24.840  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,pay_unknow,pay_unknow
    2024-06-12 11:33:24.840  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,pay_unknow,pay_unknow
    2024-06-12 11:33:24.840  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,pay_unknow,pay_unknow
    2024-06-12 11:33:24.840  INFO 20140 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin         : testFlow,1,pay_unknow,fail

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



 

 



 





