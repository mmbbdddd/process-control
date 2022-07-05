# 流程编排
 - 工程化组件：
    
     MVC架构实现前后端分工
      
     流程编排实现产品设计、架构设计、项目管理和开发的分工和统一。
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


# 使用场景
|  场景   | 是否适用  |  例子  |
|  ----  | ----  | ----  |
| 增删改查  | NO | |
| 复杂读聚合  | YES | 实时多接口聚合（数据变更频繁） |
| 数据聚合写  | YES | 高性能接口查询（数据变更不频繁性能要求，先聚合，再查询） |
| 一致性接口  | YES | 如上支付例 |
| 复杂流程  | YES | …… |
 



