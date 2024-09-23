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
 
# 最佳实践

    1，高并发系统，追求最高性能，应该采用事务消息/tcc/saga等短事务模型
    2，长流程、复杂业务，应该基于事务消息/saga这种编排风格的流程模型
    3，saga事务：事务操作，首先设置状态到容错位
    4，saga事务：正向业务、负向业务最佳在一个action中
    5，回滚确保和执行顺序逆向（pipeline架构 or fsm+事件溯源）




参考资料

 https://github.com/Azure-Samples/saga-orchestration-serverless

https://it.sohu.com/a/788243516_121389665

https://help.aliyun.com/document_detail/181208.html