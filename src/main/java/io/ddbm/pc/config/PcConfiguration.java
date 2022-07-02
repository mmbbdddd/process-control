package io.ddbm.pc.config;

import io.ddbm.pc.Pc;
import io.ddbm.pc.factory.XmlFlowFactory;
import io.ddbm.pc.utils.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class PcConfiguration {

    @Bean
    @Order(value = 1000)
    SpringUtils springUtils() {
        return new SpringUtils();
    }

    @Bean
    PcProperties pcProperties() {
        PcProperties props = new PcProperties();
        props.flowPath = "/flow";
        return props;
    }

    @Bean
    XmlFlowFactory xmlFlowFactory() {
        return new XmlFlowFactory();
    }

    @Bean
    Pc Pc() {
        return new Pc();
    }

}
