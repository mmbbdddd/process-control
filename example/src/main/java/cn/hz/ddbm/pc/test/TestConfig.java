package cn.hz.ddbm.pc.test;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ChaosService;
import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.test.support.ContainerMock;
import cn.hz.ddbm.pc.test.support.DigestLogPluginMock;
import cn.hz.ddbm.pc.test.support.MetricsTemplateMock;
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
    ChaosService chaosService() {
        return new ChaosService();
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
