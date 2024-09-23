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

    @Override
    public List<Tetrad<IdCard, String, Class<? extends FsmAction>, Router<IdCard>>> transitions() {
        return Lists.newArrayList(
                Tetrad.of(IdCard.Init, "push", MaterialCollectionAction.class, new ToRouter<>(IdCard.RuleChecked)),
                Tetrad.of(IdCard.RuleChecked, "push", RuleCheckedAction.class, new Router<>(new HashMap<String, IdCard>() {{
                    put("true", IdCard.Accepted);
                    put("false", IdCard.Init);
                }})),
                Tetrad.of(IdCard.Accepted, "push", SendBizAction.class, new Router<>(
                        new HashMap<String, IdCard>() {{
                            put("true", IdCard.RuleSyncing);
                            put("false", IdCard.Accepted);
                            put("false", IdCard.Su);
                            put("false", IdCard.Fail);
                        }})),
                Tetrad.of(IdCard.RuleSyncing, "push", SendBizAction.class, new Router<>(
                        new HashMap<String, IdCard>() {{
                            put("true", IdCard.RuleChecked);
                            put("false", IdCard.RuleSyncing);
                        }})
                )
        );
    }

## 运行

2024-09-22 02:05:25.087  INFO 12944 --- [pool-1-thread-1] flow                                     : Init
2024-09-22 02:05:25.096  INFO 12944 --- [pool-1-thread-1] flow                                     : RuleChecked
2024-09-22 02:05:25.097  INFO 12944 --- [pool-1-thread-1] flow                                     : Accepted
2024-09-22 02:05:25.098  INFO 12944 --- [pool-1-thread-1] flow                                     : Accepted
2024-09-22 02:05:25.098  INFO 12944 --- [pool-1-thread-1] flow                                     : Fail
2024-09-22 02:05:25.098  INFO 12944 --- [           main] flow                                     : 混沌测试报告：\n
2024-09-22 02:05:25.099  INFO 12944 --- [           main] flow                                     : Fail,	1

## 

# 详细手册

[什么情况下使用Fsm模式](doc/fsm模式.md)


[什么情况下使用Saga模式](doc/saga模式.md)

[混沌验证](doc/混沌验证.md)

[状态管理](doc/状态管理.md)

[会话管理](doc/fsm模式.md)

[Saga事务](doc/Saga事务.md)

[重试机制](doc/重试机制.md)

[插件和组件扩展](doc/插件和组件扩展.md) 
