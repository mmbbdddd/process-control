package hz.ddbm.pc.core.config;

import hz.ddbm.pc.core.service.PcService;
import hz.ddbm.pc.core.service.session.JvmSessionService;
import hz.ddbm.pc.core.utils.InfraUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

@Configuration
//@ComponentScan(value = "hz.ddbm.jfsm",
//        includeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {Fsm.class, Action.class, Plugin.class})
//        })
@EnableConfigurationProperties({PcProperties.class})
public class PcConfiguration {

    @Bean
    InfraUtils infraUtils() {
        return new InfraUtils();
    }

    @Bean
    PcService pcService() {
        return new PcService();
    }

    @Bean
    JvmSessionService jvmSessionService() {
        return new JvmSessionService();
    }
}
