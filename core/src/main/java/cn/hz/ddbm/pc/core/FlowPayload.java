package cn.hz.ddbm.pc.core;


import java.io.Serializable;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 21:59
 * @Version 1.0.0
 **/


public interface FlowPayload {
    Serializable getId();

    FlowStatus getStatus();

    void setStatus(FlowStatus status);
}
