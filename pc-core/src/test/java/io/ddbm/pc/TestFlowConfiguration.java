package io.ddbm.pc;

import io.ddbm.pc.chaos.ChaosService;
import io.ddbm.pc.config.FlowProperties;
import io.ddbm.pc.factory.FlowFactory;
import io.ddbm.pc.factory.provider.LocalSourceProvider;
import io.ddbm.pc.status.impl.InJvmManager;
import io.ddbm.pc.utils.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TestFlowConfiguration {

    @Bean
    ChaosService chaosService() {
        return new ChaosService();
    }

    @Bean
    FlowProperties flowProperties() {
        return new FlowProperties();
    }

    @Bean
    InJvmManager inJvmManager() {
        return new InJvmManager();
    }

    @Bean
    SpringUtils springUtils() {
        return new SpringUtils();
    }

    @Bean
    FlowFactory flowFactory() {
        return new FlowFactory();
    }

    @Bean
    LocalSourceProvider localSourceProvider() {
        return new LocalSourceProvider();
    }

    @Bean
    ProcessControlService flowService() {
        return new ProcessControlService();
    }

}
