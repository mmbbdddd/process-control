package cn.hz.ddbm.pc.newcore.chaos;


import cn.hutool.core.util.IdcardUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.exception.SessionException;
import cn.hz.ddbm.pc.newcore.factory.BeanSagaFlowFactory;
import cn.hz.ddbm.pc.newcore.fsm.FsmState;
import cn.hz.ddbm.pc.newcore.fsm.FsmWorker;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class ChaosServiceTest {

    AnnotationConfigApplicationContext ctx         = new AnnotationConfigApplicationContext();
    ChaosService                       chaosService;

    @BeforeEach
    public void setup() {
        ctx.register(SS.class);
        ctx.refresh();
        chaosService = ctx.getBean(ChaosService.class);
    }


    @Test
    public void saga() {
        try {
            chaosService.chaos(
                    "test",
                    new ChaosService.MockPayLoad(  new SagaState(1, SagaWorker.Offset.task)),
                    ChaosConfig.goodOf(true,1,10,3000));
        }  catch (SessionException e) {
            throw new RuntimeException(e);
        }   catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void fsm() {
        try {
            chaosService.chaos(
                    "test",
                    new ChaosService.MockPayLoad(  new FsmState(_fsm.init, FsmWorker.Offset.task)),
                    ChaosConfig.goodOf(true,1,10,3000));
        }  catch (SessionException e) {
            throw new RuntimeException(e);
        }   catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    static class SS {
        @Bean
        LocalChaosAction localChaosAction() {
            return new LocalChaosAction();
        }


        @Bean
        SpringUtil springUtil() {
            return new SpringUtil();
        }
        @Bean
        BeanSagaFlowFactory sagaFlowFactory() {
            return new BeanSagaFlowFactory();
        }

        @Bean
        ProcessorService procesorService() {
            return new ProcessorService();
        }

        @Bean
        ChaosService chaosService() {
            return new ChaosService();
        }

    }


    enum _fsm{
        init;
    }
}