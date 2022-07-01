package io.ddbm.pc.config;

import io.ddbm.pc.factory.XmlFlowFactory;
import io.ddbm.pc.utils.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PcConfiguration {
    @Bean
    PcProperties pcProperties() {
        return new PcProperties();
    }
//
//    @Bean
//    XmlFlowFactory xmlFlowFactory() {
//        return new XmlFlowFactory();
//    }

    @Bean
    SpringUtils springUtils() {
        return new SpringUtils();
    }
}
