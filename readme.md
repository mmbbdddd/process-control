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
## 单元测试

    @Test
    public void chaos() throws Exception {
        try {
            //执行100此，查看流程中断概率
            chaosService.chaos("idcard",
                    new ChaosService.MockPayLoad(new FsmState(IdCard.Init, FsmWorker.Offset.task)),
                        //执行100次
                    new ChaosConfig(true, 1, 100, 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

## 运行

![img.png](img.png)      

## 

# 详细手册

[流程编排](doc/saga模式.md) 

[混沌验证](doc/混沌验证.md)

[状态管理](doc/状态管理.md)

[会话管理](doc/会话管理.md) 

[重试机制](doc/重试机制.md)

[插件和组件扩展](doc/插件和组件扩展.md) 
