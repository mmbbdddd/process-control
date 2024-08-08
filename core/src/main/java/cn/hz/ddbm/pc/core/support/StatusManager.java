package cn.hz.ddbm.pc.core.support;


import cn.hz.ddbm.pc.core.FlowStatus;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Description TODO
 * @Author wanglin
 * @Date 2024/8/7 21:29
 * @Version 1.0.0
 **/


public interface StatusManager {
    String code();
    void updateStatus(String flow, Serializable flowId, FlowStatus flowStatus) throws IOException;
}
