package cn.hz.ddbm.pc.test;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.Action;
import cn.hz.ddbm.pc.core.support.ExpressionEngine;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.lock.JdkLocker;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.session.memory.MemorySessionManager;
import cn.hz.ddbm.pc.status.memory.MemoryStatusManager;
import cn.hz.ddbm.pc.test.support.DigestLogPluginMock;
import cn.hz.ddbm.pc.test.support.ExpressionEngineMock;
import cn.hz.ddbm.pc.test.support.MetricsTemplateMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    ExpressionEngine expressionEngine() {
        return new ExpressionEngineMock();
    }

    @Bean
    InfraUtils infraUtils() {
        return new InfraUtils();
    }

    @Bean
    JdkLocker jdkLocker() {
        return new JdkLocker();
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
        return new MemorySessionManager(256, 2);
    }

    @Bean
    StatusManager statusManager() {
        return new MemoryStatusManager(256, 2);
    }
}
