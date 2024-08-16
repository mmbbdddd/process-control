package cn.hz.ddbm.pc.container.chaos

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import

@EnableAspectJAutoProxy
@SpringBootApplication
@Import(ChaosTestConfig.class)
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


}
