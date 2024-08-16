package cn.hz.ddbm.pc.core.support;


/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 22:34
 * @Version 1.0.0
 **/


public interface StatisticsSupport {
    void increment(String windows);

    Integer get(String windows);
}
