# 简介

**流程编排：积木化组装复杂、长流程业务**

您将获得如下好处

1. 业务能力组件化
2. 低耦合、变更友好
3. 随需而变、灵活组装
4. 一致性，Saga事务
5. 高性能
6. 流程和业务分离；纲举目张
7. 高可读、可维护

# 快速使用

## 引入pom

        <dependency>
            <groupId>cn.hz.ddbm.pc</groupId>
            <artifactId>framework</artifactId>
            <version>${pc.version}</version>
        </dependency>

## 编排业务
    
    这是一个小例子（身份证办理），旨在演示一个比较复杂的流程，用流程编排是如何实现的。
![img.png](doc%2Fimg.png)

     @Override
    public List<Triple<IdCard,   Class<? extends FsmAction>, Router<IdCard>>> transitions() {
        return Lists.newArrayList(
                //收集客户材料
                Triple.of(IdCard.Init, MaterialCollectionAction.class, new ToRouter<>(IdCard.RuleChecked)),
                //检测客户资料
                Triple.of(IdCard.RuleChecked, RuleCheckedAction.class, new Router<>(new RowKeyTable<String, IdCard, Double>() {{
                    put("result.code==0000", IdCard.Accepted, 0.9);//0.9是mock环境下发生的概率。在生产环境可以没有这个参数。
                    put("result.code==0008", IdCard.Init, 0.1);
                }})),
                //身份证办理
                Triple.of(IdCard.Accepted, SubmitProcessAction.class, new Router<>(
                        new RowKeyTable<String, IdCard, Double>() {{
                            put("result.code==0002", IdCard.RuleSyncing, 0.1);
                            put("result.code==0003", IdCard.Accepted, 0.1);
                            put("result.code==0000", IdCard.Su, 0.7);
                            put("result.code==00001", IdCard.Fail, 0.1);
                        }})),
                //规则更新后继续办理
                Triple.of(IdCard.RuleSyncing, SubmitProcessAction.class, new Router<>(
                        new RowKeyTable<String, IdCard, Double>() {{
                            put("result.code==0000", IdCard.RuleChecked, 0.9);
                            put("result.code==0009", IdCard.RuleSyncing, 0.1);
                        }})
                )
        );
    }

    1，遵从了流程和逻辑分离的设计理念，可以在Action没有进行开发完成的情况下，验证整体业务流程。在整体设计和验证完毕后，各action接口明确，并行开发。
    2，相比传统的消息驱动的流程，整体流程架构可视，局部逻辑组件化程度更好，可编排、变更透明，更安全
    3，相比seata等json dsl的编排风格。JavaDsl基于静态代码检查、健壮性更好。 

## 单元测试

    @Test
    public void chaos() throws Exception {
        try {
            //执行100次，看结果分布概率
            chaosService.chaos("idcard",
                    new ChaosService.MockPayLoad(new FsmState(IdCard.Init, FsmWorker.Offset.task)),
                        //执行100次
                    new ChaosConfig(true, 1, 100, 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

## 运行

    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-2] flow                                     : 1,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-3] flow                                     : 2,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-9] flow                                     : 8,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-1] flow                                     : 0,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-7] flow                                     : 6,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-5] flow                                     : 4,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-6] flow                                     : 5,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-8] flow                                     : 7,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [ool-2-thread-10] flow                                     : 9,Init
    2024-09-26 09:16:23.077  INFO 37364 --- [pool-2-thread-4] flow                                     : 3,Init
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-8] flow                                     : 7,RuleChecked
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-1] flow                                     : 0,RuleChecked
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-2] flow                                     : 1,RuleChecked
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-9] flow                                     : 8,RuleChecked
    2024-09-26 09:16:23.088  INFO 37364 --- [ool-2-thread-10] flow                                     : 9,RuleChecked
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-3] flow                                     : 2,RuleChecked
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-7] flow                                     : 6,RuleChecked
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-8] flow                                     : 7,Accepted
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-4] flow                                     : 3,RuleChecked
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-5] flow                                     : 4,RuleChecked
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-3] flow                                     : 2,Accepted
    2024-09-26 09:16:23.088  INFO 37364 --- [pool-2-thread-6] flow                                     : 5,RuleChecked
    2024-09-26 09:16:23.090  INFO 37364 --- [ool-2-thread-10] flow                                     : 9,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-9] flow                                     : 8,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-5] flow                                     : 4,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-2] flow                                     : 1,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-7] flow                                     : 6,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-4] flow                                     : 3,Init
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-6] flow                                     : 5,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-1] flow                                     : 0,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-9] flow                                     : 8,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-8] flow                                     : 7,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-3] flow                                     : 2,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-2] flow                                     : 1,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-5] flow                                     : 4,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-7] flow                                     : 6,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-6] flow                                     : 5,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-9] flow                                     : 8,Su
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-8] flow                                     : 7,Su
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-3] flow                                     : 2,Su
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-2] flow                                     : 1,Su
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-5] flow                                     : 4,Fail
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-7] flow                                     : 6,Fail
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-4] flow                                     : 3,RuleChecked
    2024-09-26 09:16:23.090  INFO 37364 --- [ool-2-thread-10] flow                                     : 9,Accepted
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-6] flow                                     : 5,Fail
    2024-09-26 09:16:23.090  INFO 37364 --- [ool-2-thread-10] flow                                     : 9,Su
    2024-09-26 09:16:23.090  INFO 37364 --- [pool-2-thread-4] flow                                     : 3,Accepted
    2024-09-26 09:16:23.091  INFO 37364 --- [pool-2-thread-1] flow                                     : 0,Accepted
    2024-09-26 09:16:23.091  INFO 37364 --- [pool-2-thread-4] flow                                     : 3,Accepted
    2024-09-26 09:16:23.091  INFO 37364 --- [pool-2-thread-1] flow                                     : 0,Su
    2024-09-26 09:16:23.091  INFO 37364 --- [pool-2-thread-4] flow                                     : 3,Su
    2024-09-26 09:16:23.092  INFO 37364 --- [           main] flow                                     : 混沌测试报告：\n
    2024-09-26 09:16:23.092  INFO 37364 --- [           main] flow                                     : Su,	7
    2024-09-26 09:16:23.092  INFO 37364 --- [           main] flow                                     : Fail,	3

查看某个的执行结果

![img.png](img.png)
 