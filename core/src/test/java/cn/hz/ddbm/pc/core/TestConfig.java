package cn.hz.ddbm.pc.core;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    InfraUtils infraUtils(ContainerMock container) {
        return new InfraUtils(container);
    }

    @Bean
    SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    ContainerMock container() {
        return new ContainerMock();
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
    DigestLogPluginMock digestLogPlugin() {
        return new DigestLogPluginMock();
    }
}
