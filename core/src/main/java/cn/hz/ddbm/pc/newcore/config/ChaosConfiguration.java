package cn.hz.ddbm.pc.newcore.config;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.chaos.ChaosHandler;
import cn.hz.ddbm.pc.newcore.chaos.LocalChaosAction;
import cn.hz.ddbm.pc.newcore.chaos.RemoteChaosAction;
import cn.hz.ddbm.pc.newcore.factory.BeanFsmFlowFactory;
import cn.hz.ddbm.pc.newcore.factory.BeanSagaFlowFactory;
import cn.hz.ddbm.pc.newcore.infra.InfraUtils;
import cn.hz.ddbm.pc.newcore.infra.StatisticsSupport;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmLocker;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmSessionManager;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmStatisticsSupport;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmStatusManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


@EnableAspectJAutoProxy
public class ChaosConfiguration {

    @Bean
    ProcessorService processorService() {
        return new ProcessorService();
    }
    @Bean
    RemoteChaosAction remoteChaosAction() {
        return new RemoteChaosAction();
    }

    @Bean
    LocalChaosAction localChaosAction() {
        return new LocalChaosAction();
    }



    @Bean
    ExecutorService actionExecutorService() {
        return new ScheduledThreadPoolExecutor(10);
    }



    @Bean
    JvmStatusManager jvmStatusManager() {
        return new JvmStatusManager();
    }

    @Bean
    JvmSessionManager jvmSessionManager() {
        return new JvmSessionManager();
    }

    @Bean
    ChaosHandler chaosHandler() {
        return new ChaosHandler();
    }


    @Bean
    JvmLocker jvmLocker() {
        return new JvmLocker();
    }


    @Bean
    InfraUtils infraUtils() {
        return new InfraUtils();
    }

    @Bean
    SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    StatisticsSupport statisticsSupport() {
        return new JvmStatisticsSupport();
    }
}

