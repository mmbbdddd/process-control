package cn.hz.ddbm.pc.newcore.chaos;


import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.exception.FlowEndException;
import cn.hz.ddbm.pc.newcore.exception.InterruptedException;
import cn.hz.ddbm.pc.newcore.exception.PauseException;
import cn.hz.ddbm.pc.newcore.exception.SessionException;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlowTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class ChaosServiceTest {

    AnnotationConfigApplicationContext ctx         = new AnnotationConfigApplicationContext();
    ChaosService                       chaosService;
    ChaosConfig                        chaosConfig = ChaosConfig.goodOf();

    @Before
    public void setup() {
        ctx.register(SS.class);
        ctx.refresh();
        chaosService = ctx.getBean(ChaosService.class);
    }


    @Test
    public void saga() {
        try {
            chaosService.saga(null, true, 1, 1, 3000, chaosConfig);
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
        ProcessorService procesorService() {
            return new ProcessorService();
        }

        @Bean
        ChaosService chaosService() {
            return new ChaosService();
        }

        @Bean
        ChaosHandler chaosHandler() {
            return new ChaosHandler();
        }
    }


}