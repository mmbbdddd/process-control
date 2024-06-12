package hz.ddbm.pc.core;

import hz.ddbm.pc.core.domain.Flow;

import java.io.Serializable;

/**
 * 运行时上下文session存储服务，有两种实现
 * 1，jvm：适用于单机、测试
 * 2，redis：适用于分布式
 */
public interface SessionService {


    void put(Flow flow, Serializable id, String key, Object value);

    Object get(Flow flow, Serializable id,String key);


}
