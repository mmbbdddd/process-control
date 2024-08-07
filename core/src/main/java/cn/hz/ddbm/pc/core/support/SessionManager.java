package cn.hz.ddbm.pc.core.support;


import java.io.Serializable;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 21:29
 * @Version 1.0.0
 **/


public interface SessionManager {
    void set(String flowName,String flowId,String key,Object value);

    Object get(String flowName,String flowId,String key);
}
