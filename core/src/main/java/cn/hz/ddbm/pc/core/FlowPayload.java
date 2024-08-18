package cn.hz.ddbm.pc.core;


import java.io.Serializable;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 21:59
 * @Version 1.0.0
 **/


public interface FlowPayload<S extends Enum<S>> {
    Serializable getId();

    Node<S> getStatus();

    void setStatus(Node<S> status);
}
