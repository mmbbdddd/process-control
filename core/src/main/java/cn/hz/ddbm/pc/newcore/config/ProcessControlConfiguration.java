package cn.hz.ddbm.pc.newcore.config;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.infra.StatisticsManager;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmLockManager;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmSessionManager;
import cn.hz.ddbm.pc.newcore.infra.impl.SimpleStatisticsManager;
import cn.hz.ddbm.pc.newcore.infra.impl.JvmStatusManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


@EnableAspectJAutoProxy
public class ProcessControlConfiguration {
    @Bean
    SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    ProcessorService processorService() {
        return new ProcessorService();
    }


    @Bean
    ScheduledThreadPoolExecutor pluginExecutorService() {
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
    JvmLockManager jvmLockManager() {
        return new JvmLockManager();
    }

    @Bean
    SimpleStatisticsManager simpleStatisticsManager() {
        return new SimpleStatisticsManager();
    }
}

