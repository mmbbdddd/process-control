package cn.hz.ddbm.pc.container.chaos;

import cn.hz.ddbm.pc.container.ChaosAspect;
import cn.hz.ddbm.pc.container.SpringContainer
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.profile.chaos.ChaosRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ChaosTestConfig {
    @Bean
    Container chaosContainer() {
        return new SpringContainer();
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
        return new ChaosPcService() {
            @Override
            public List<ChaosRule> chaosRules() {
                return new ArrayList<ChaosRule>() {{
                    add(new ChaosRule("true",0.5,[IOException.class,RuntimeException.class]));
                }};
            }
        };
    }
}
