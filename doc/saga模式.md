# saga模式介绍

论文：https://www.cs.cornell.edu/andru/cs711/2002fa/reading/sagas.pdf

# saga模式的优缺点

**和非saga框架的比较**

    业务直接提交，短事务模式，具有比较好的性能  vs  长事务模式，性能差
    隔离性比较差                             vs  比较好的隔离性
    回滚需要应用编写代码                      vs  应用透明
    最终一致性                               vs  全局一致性 
    回滚基于补偿代码                          vs  回滚基于数据库sql 



**和saga框架的比较**

![img_1.png](sagaidff.png)

1，seata没有做防空补偿和悬挂事务，processctrl支持防空补偿和悬挂事务

2，seata的业务逻辑是基于pipeline模式的（即processorctrl中的saga模式），而不是任意流程形状。

 
# 最佳实践

    1， 最佳所有业务都使用fsm模式，pipeline架构在fsm的事件溯源验证成熟后将标记为不推荐
    2， fsm模式本质上是事件消息+saga的升级版
        2.1  流程编排从应用管理   == 》框架管理 
        2.2  状态管理从应用管理   == 》框架管理 
        2.3  基于模板方法（流程编排版），使业务开发聚焦于业务 
        2.4  基于事件溯源、提供逆向saga正向和逆向事务状态管理，确保业务一致性

    3， 短期看pipeline是可靠的————pipeline架构清晰、简单、容易编排&确保一致性




参考资料

https://github.com/Azure-Samples/saga-orchestration-serverless

https://it.sohu.com/a/788243516_121389665

https://help.aliyun.com/document_detail/181208.html