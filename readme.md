# 流程编排
# 快速开始
 **业务场景**
![img.png](img.png)
 **代码**

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


     2024-06-12 10:40:58.527  INFO 21704 --- [           main] hz.ddbm.pc.core.PcService              : 初始化流程:testFlow
     2024-06-12 10:40:59.045  INFO 21704 --- [           main] hz.ddbm.pc.core.PcServiceTest          : Started PcServiceTest in 1.174 seconds (JVM running for 1.826)
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,init,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow
     2024-06-12 10:40:59.171  INFO 21704 --- [           main] hz.ddbm.pc.core.plugin.LogPlugin       : testFlow,1,pay_unknow,pay_unknow


# 功能
 - [复杂、长业务流程积木化、组件化开发](doc/组件化.md)
 - [高性能SAGA事务](doc/SAGA事务.md)
 - 插件化可插拔功能，包括：日志、监控、告警、统计……等 
 - 动态发布

# 特性

# demo





