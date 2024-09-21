package cn.hz.ddbm.pc.fsm;


import cn.hz.ddbm.pc.fsm.actions.MaterialCollectionAction;
import cn.hz.ddbm.pc.fsm.actions.RuleCheckedAction;
import cn.hz.ddbm.pc.fsm.actions.SendBizAction;
import cn.hz.ddbm.pc.newcore.chaos.ChaosConfig;
import cn.hz.ddbm.pc.newcore.chaos.ChaosService;
import cn.hz.ddbm.pc.newcore.config.ChaosConfiguration;
import cn.hz.ddbm.pc.newcore.factory.BeanFsmFlowFactory;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.FsmWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@ComponentScan("cn.hz.ddbm.pc.idcardapply.actions")
@SpringBootTest
@Import({IdCardTest.CC.class, ChaosConfiguration.class})
@RunWith(SpringRunner.class)
public class IdCardTest {
    @Autowired
    ChaosService chaosService;

    ChaosConfig chaosConfig = ChaosConfig.goodOf();

    @Test
    public void chaos() throws Exception {

        try {
            //执行100此，查看流程中断概率
            chaosService.chaos("idcard", false, 3, 1, 4,
                    new ChaosService.MockPayLoad(1, new FsmState(IdCard.Init, FsmWorker.Offset.task)),
                    chaosConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class CC {
        @Bean
        BeanFsmFlowFactory fsmFlowFactory() {
            return new BeanFsmFlowFactory();
        }

//        @Bean
//        BeanSagaFlowFactory sagaFlowFactory() {
//            return new BeanSagaFlowFactory();
//        }
        @Bean
        MaterialCollectionAction materialCollectionAction() {
            return new MaterialCollectionAction();
        }

        @Bean
        RuleCheckedAction ruleCheckedAction() {
            return new RuleCheckedAction();
        }

        @Bean
        SendBizAction sendBizAction() {
            return new SendBizAction();
        }

        @Bean
        IdCardFlow idCard() {
            return new IdCardFlow();
        }

        @Bean
        ChaosService chaosService() {
            return new ChaosService();
        }

    }
}
