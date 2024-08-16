package cn.hz.ddbm.pc.configuration;

import cn.hz.ddbm.pc.container.ChaosAspect;
import cn.hz.ddbm.pc.container.SpringContainer;
import cn.hz.ddbm.pc.container.chaos.ChaosHandler;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.support.ExpressionEngine;
import cn.hz.ddbm.pc.core.support.Locker;
import cn.hz.ddbm.pc.core.support.StatisticsSupport;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.lock.JdkLocker;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.profile.PcService;
import cn.hz.ddbm.pc.session.memory.MemorySessionManager;
import cn.hz.ddbm.pc.status.memory.MemoryStatusManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ConditionalOnClass({PcService.class})
@EnableConfigurationProperties({RedisProperties.class})
@EnableAspectJAutoProxy
public class PcChaosConfiguration {
    @Bean
    ChaosPcService pcService() {
        return new ChaosPcService();
    }

    @Bean
    MemoryStatusManager memoryStatusManager() {
        return new MemoryStatusManager(256,1);
    }

    @Bean
    MemorySessionManager memorySessionManager() {
        return new MemorySessionManager(256,1);
    }

    @Bean
    ChaosHandler chaosHandler(ChaosPcService pcService) {
        return new ChaosHandler(pcService);
    }

    @Bean
    ChaosAspect aspect() {
        return new ChaosAspect();
    }

    @Bean
    Container container() {
        return new SpringContainer();
    }

    @Bean
    Locker locker() {
        return new JdkLocker();
    }

    @Bean
    ExpressionEngine expressionEngine() {
        return new ExpressionEngine();
    }

    @Bean
    InfraUtils infraUtils(Container container) {
        return new InfraUtils(container);
    }

    @Bean
    StatisticsSupport metricsTemplate(Container container) {
        return new StatisticsSupport() {
            @Override
            public void increment(String windows) {
//todo
            }

            @Override
            public Long get(String windows) {
                return null;
            }
        };
    }


}
