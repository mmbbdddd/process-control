package cn.hz.ddbm.pc.configuration;

import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.profile.PcService;
import cn.hz.ddbm.pc.profile.StablePcService;
import cn.hz.ddbm.pc.session.memory.MemorySessionManager;
import cn.hz.ddbm.pc.session.redis.RedisSessionManager;
import cn.hz.ddbm.pc.status.memory.MemoryStatusManager;
import cn.hz.ddbm.pc.status.redis.RedisStatusManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@ConditionalOnClass({PcService.class})
@EnableConfigurationProperties({RedisProperties.class})
public class PcChaosConfiguration {
    @Bean
    StablePcService pcService() {
        return new StablePcService();
    }

    @Bean
    MemoryStatusManager memoryStatusManager() {
        return new MemoryStatusManager();
    }

    @Bean
    MemorySessionManager memorySessionManager() {
        return new MemorySessionManager();
    }


}
