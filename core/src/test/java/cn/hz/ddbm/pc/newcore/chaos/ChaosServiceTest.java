package cn.hz.ddbm.pc.newcore.chaos;


import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.exception.FlowEndException;
import cn.hz.ddbm.pc.newcore.exception.InterruptedException;
import cn.hz.ddbm.pc.newcore.exception.PauseException;
import cn.hz.ddbm.pc.newcore.exception.SessionException;
import cn.hz.ddbm.pc.newcore.factory.BeanSagaFlowFactory;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class ChaosServiceTest {

    AnnotationConfigApplicationContext ctx         = new AnnotationConfigApplicationContext();
    ChaosService                       chaosService;
    ChaosConfig                        chaosConfig = ChaosConfig.goodOf();

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
                    chaosConfig);
        } catch (PauseException e) {
            throw new RuntimeException(e);
        } catch (SessionException e) {
            throw new RuntimeException(e);
        } catch (FlowEndException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void fsm() {
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


}