package cn.hz.ddbm.pc.statistics;

import cn.hutool.core.lang.Assert;
import cn.hz.ddbm.pc.core.support.StatisticsSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisStatistics implements StatisticsSupport {

    Integer cacheSize;
    Integer hours;

    public RedisStatistics(Integer cacheSize, Integer hours) {
        Assert.notNull(cacheSize, "cacheSize is null");
        Assert.notNull(hours, "hours is null");
        this.cacheSize = cacheSize;
        this.hours     = hours;
    }

    @Autowired
    RedisTemplate<String, Long> redisTemplate;

    @Override
    public void increment(String windows) {
        redisTemplate.opsForValue().increment(windows);
    }

    @Override
    public Long get(String windows) {
        return redisTemplate.opsForValue().get(windows);
    }
}
