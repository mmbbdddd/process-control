package cn.hz.ddbm.pc.test;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.support.ExpressionEngine;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.session.memory.MemorySessionManager;
import cn.hz.ddbm.pc.status.memory.MemoryStatusManager;
import cn.hz.ddbm.pc.test.support.ContainerMock;
import cn.hz.ddbm.pc.test.support.DigestLogPluginMock;
import cn.hz.ddbm.pc.test.support.ExpressionEngineMock;
import cn.hz.ddbm.pc.test.support.MetricsTemplateMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class TestConfig {
    @Bean
    ExpressionEngine expressionEngine(ContainerMock container) {
        return new ExpressionEngineMock();
    }

    @Bean
    InfraUtils infraUtils(ContainerMock container) {
        return new InfraUtils(container);
    }

    @Bean
    SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    ChaosPcService chaosPcService() {
        return new ChaosPcService();
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

    @Bean
    SessionManager sessionManager() {
        return new MemorySessionManager();
    }

    @Bean
    StatusManager statusManager() {
        return new MemoryStatusManager();
    }
}
