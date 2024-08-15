package cn.hz.ddbm.pc.configuration;

import cn.hz.ddbm.pc.profile.PcService;
import cn.hz.ddbm.pc.profile.StablePcService;
import cn.hz.ddbm.pc.session.memory.MemorySessionManager;
import cn.hz.ddbm.pc.session.redis.RedisSessionManager;
import cn.hz.ddbm.pc.status.memory.MemoryStatusManager;
import cn.hz.ddbm.pc.status.redis.RedisStatusManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass({PcService.class})
public class PcStableConfiguration {

    @Bean
    StablePcService pcService() {
        return new StablePcService();
    }

    @Bean
    MemoryStatusManager memoryStatusManager() {
        return new MemoryStatusManager();
    }

    @Bean
    RedisStatusManager redisStatusManager() {
        return new RedisStatusManager();
    }

    @Bean
    MemorySessionManager memorySessionManager() {
        return new MemorySessionManager();
    }

    @Bean

    @ConditionalOnExpression("#{dddd.pc.status-manager-redis.enable:true}")
    RedisSessionManager redisSessionManager() {
        return new RedisSessionManager();
    }
}
