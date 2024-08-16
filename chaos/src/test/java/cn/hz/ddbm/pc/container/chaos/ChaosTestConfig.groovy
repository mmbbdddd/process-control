package cn.hz.ddbm.pc.container.chaos

import cn.hz.ddbm.pc.container.ChaosAspect
import cn.hz.ddbm.pc.profile.ChaosPcService
import cn.hz.ddbm.pc.profile.chaos.ChaosRule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class ChaosTestConfig {


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
        return new ChaosPcService() {
            @Override
            public List<ChaosRule> chaosRules() {
                return new ArrayList<ChaosRule>() {
                    {
                        add(new ChaosRule("true", 0.5, [IOException.class, RuntimeException.class]));
                    }
                };
            }
        };
    }
}
