package io.ddbm.pc.config;

import io.ddbm.pc.Pc;
import io.ddbm.pc.factory.XmlFlowFactory;
import io.ddbm.pc.utils.SpringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableConfigurationProperties({PcProperties.class})
public class PcConfiguration {

    @Bean
    @Order(value = 1000)
    SpringUtils springUtils() {
        return new SpringUtils();
    }

    @Bean
    XmlFlowFactory xmlFlowFactory() {
        return new XmlFlowFactory();
    }

    @Bean
    Pc Pc() {
        return new Pc();
    }

    @Bean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
    public SimpleApplicationEventMulticaster myEventMulticaster(PcProperties props) {
        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
        simpleApplicationEventMulticaster.setTaskExecutor(taskExecutor(props));
        return simpleApplicationEventMulticaster;
    }

    @Bean
    @ConditionalOnMissingBean(value = {ThreadPoolTaskExecutor.class})
    public TaskExecutor taskExecutor(PcProperties props) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(props.corePoolSize);
        executor.setMaxPoolSize(props.maxPoolSize);
        executor.setQueueCapacity(props.queueCapacity);
        executor.setKeepAliveSeconds(props.keepAliveSeconds);
        executor.setThreadNamePrefix("Pc-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }


}
