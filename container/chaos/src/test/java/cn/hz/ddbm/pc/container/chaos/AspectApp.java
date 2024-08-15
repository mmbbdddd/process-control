package cn.hz.ddbm.pc.container.chaos;

import cn.hz.ddbm.pc.container.ChaosAspect;
import cn.hz.ddbm.pc.container.ChaosContainer;
import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class AspectApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AspectApp.class, args);
    }

    @Autowired
    ChaosAopAction action;
    @Override
    public void run(String... args) throws Exception {
        action.execute(null);
    }



    static class CC {
        @Bean
        ChaosContainer chaosContainer() {
            return new ChaosContainer();
        }

        @Bean
        ChaosAopAction action() {
            return new ChaosAopAction();
        }

        @Bean
        ChaosHandler chaosHandler() {
            return new ChaosHandler();
        }
        @Bean
        ChaosAspect aspect() {
            return new ChaosAspect();
        }

        @Bean
        ChaosPcService pcService() {
            return new ChaosPcService();
        }
    }

    static class ChaosAopAction implements Action {

        @Override
        public String beanName() {
            return null;
        }

        @Override
        public void execute(FlowContext<?> ctx) throws Exception {
            System.out.println("xx");
        }
    }
}
