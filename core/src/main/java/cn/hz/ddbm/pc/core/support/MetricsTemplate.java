package cn.hz.ddbm.pc.core.support;


import java.util.Date;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 22:34
 * @Version 1.0.0
 **/


public interface MetricsTemplate {
    void increment(String windows);

    Integer get(String windows);
}
