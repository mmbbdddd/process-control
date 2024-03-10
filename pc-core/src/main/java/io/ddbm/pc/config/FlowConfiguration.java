package io.ddbm.pc.config;

import io.ddbm.pc.ProcessControlService;
import io.ddbm.pc.factory.FlowFactory;
import io.ddbm.pc.factory.provider.LocalSourceProvider;
import io.ddbm.pc.utils.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Configuration
public class FlowConfiguration {
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
    @Order(Integer.MIN_VALUE)
    ProcessControlService flowService() {
        return new ProcessControlService();
    }
}
