# 流程编排
 - 关注点分离组件：
    流程编排实现产品设计、架构设计、项目管理和开发的分工协作。 
 - 积木化开发 
 - saga分布式事务
 

# DEMO
```xml
<?xml version="1.0" encoding="utf-8"?>
<flow name="simple" xmlns="http://ddbm.io/pc.xsd">
    <!--    支付交易-->
    <start name="init">
        <on event="next" action="validateAction,initAction" maybe="pay"></on>
    </start>
    <end name="su"/>
    <end name="cancel"/>
    <end name="fail"/>
    <node name="pay" fluent="false">
        <on event="next" action="payAction" maybe="su,fail,payQuery"></on>
    </node>
    <!--    支付查询-->
    <node name="payQuery">
        <on event="next" action="payQueryAction" retry="10" maybe="su,fail,rengong"></on>
    </node>
    <!--    人工处理-->
    <node name="rengong">
        <on event="next" action="passAction" maybe="su"></on>
        <on event="fail" action="failAction" maybe="fail"></on>
    </node>
    <!--    节点路由规则-->

    <plugins>
        <!--        <plugin name="catPlugin"/>-->
        <!--        <plugin name="sentinelPlugin"/>-->
    </plugins>
</flow>

```

```JAVA
    @Test
    public void test1() {
        pc.chaos("simple",new SimpleOrder(), null)
    }
```

```text
         21:25:17.432 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'Pc'
        21:25:17.435 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata'
        21:25:17.457 [main] INFO digest - flow:simple,id:null,from:init,event:next,action:validateAction,initAction,to:pay
        21:25:17.458 [main] WARN digest - flow:simple,id:null,from:pay,event:next,action:payAction,pause:随机出个暂停异常
        21:25:17.458 [main] INFO digest - flow:simple,id:null,from:pay,event:next,action:payAction,to:payQuery
        21:25:17.459 [main] INFO digest - flow:simple,id:null,from:payQuery,event:next,action:payQueryAction,to:rengong
        21:25:17.459 [main] INFO digest - flow:simple,id:null,from:rengong,event:next,action:passAction,to:su
``` 


# 技术场景
|  场景   | 是否适用  |  例子  |
|  ----  | ----  | ----  |
| 增删改查  | NO | |
| 复杂读聚合  | YES | 实时多接口聚合，推荐适用sync方法 |
| 数据聚合写  | YES | 高性能查询，推荐适用async方法，先聚合落地。再查询。 |
| 一致性接口  | YES | 如上支付例 |
| 复杂流程  | YES | …… | 

# 业务场景
特别适用于金融级、企业级业务流程
 - 1，服务可用性、准确性要求特别高  
 - 2，业务变更频发 
 - 3，代码维护难、扩展难
 - 4，安全问题大于天

具体使用案例
 - 1，产品设计节点一边沟通，一边编排流程，沟通完毕无需代码开发，调用chaos方法即可验证流程本身的通畅性和健壮性。
 - 2，在流程基础上，架构师设计监控、告警、降级、限流、插件、组件……实现业务和技术分离。 满足系统健壮性、可观测性、扩展性、可维护性……要求。
 - 3，开发管理：一个xml文件，明了功能点，改动点，风险。
 - 4，发布上线：如有代码异常，最后情况下流程可容错降级。
 - 5，维护：一个xml掌控业务流程，业务风险。



 
 



