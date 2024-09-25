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
                Tetrad.of(IdCard.RuleChecked, "push", RuleCheckedAction.class, new Router<>(new RowKeyTable<String, IdCard,Double>() {{
                    put("result.code==0000", IdCard.Accepted,0.9);
                    put("result.code==0008", IdCard.Init,0.1);
                }})),
                Tetrad.of(IdCard.Accepted, "push", SendBizAction.class, new Router<>(
                        new RowKeyTable<String, IdCard,Double>() {{
                            put("result.code==0002", IdCard.RuleSyncing,0.1);
                            put("result.code==0003", IdCard.Accepted,0.1);
                            put("result.code==0000", IdCard.Su,0.7);
                            put("result.code==00001", IdCard.Fail,0.1);
                        }})),
                Tetrad.of(IdCard.RuleSyncing, "push", SendBizAction.class, new Router<>(
                        new RowKeyTable<String, IdCard,Double>() {{
                            put("result.code==0000", IdCard.RuleChecked,0.9);
                            put("result.code==0009", IdCard.RuleSyncing,0.1);
                        }})
                )
        );
    }

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

   ![img.png](img.png)

## 
 