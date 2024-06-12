package hz.ddbm.pc.core;

import hz.ddbm.pc.core.domain.BizContext;
import hz.ddbm.pc.core.domain.Flow;

import java.io.Serializable;

/**
 * 流程的状态存储，有两种
 * 1，流程状态：Flow.STAUS
 * 2，流程执行状态：参见流程node.name
 */
public interface StatusService {

    void updateNodeState(Flow flow, Serializable id, String nodeStatus, BizContext ctx);

    void updateFlowStatus(Flow flow, Serializable id, Flow.STAUS flowStatus, BizContext ctx);

    String getNodeStatus(Flow flow, Serializable id);

    Flow.STAUS getFlowStatus(Flow flow, Serializable id);
}
