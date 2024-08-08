package cn.hz.ddbm.pc.core;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TestConfig {
    @Bean
    InfraUtils infraUtils(Container container) {
        return new InfraUtils(container);
    }

    @Bean
    SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    ContainerSpring container() {
        return new ContainerSpring();
    }
    @Bean
    Action testAction() {
        return new TestAction();
    }
    @Bean
    MetricsTemplateMock metricsTemplateMock() {
        return new MetricsTemplateMock();
    }
    @Bean
    DigestLogPlugin digestLogPlugin() {
        return new DigestLogPlugin();
    }
}
