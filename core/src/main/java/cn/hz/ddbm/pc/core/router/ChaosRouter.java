package cn.hz.ddbm.pc.core.router;

import cn.hz.ddbm.pc.core.Event;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.Router;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ChaosRouter implements Router<Set<String>> {
    @Override
    public String routerName() {
        return "chaosRouter";
    }

    /**
     * 产生任意可能的路由结果返回
     * 1，流程定义内的node
     * 2，流程内没有定义的node——任意字符串
     * 3，任意异常
     * 内部异常
     * IllegalEntityException
     * IllegalFunctionException
     * InterruptedFlowException
     * NoRouterResultException
     * PauseFlowException
     * 开放异常
     * RuntimeException
     * IoException
     * 参数异常
     * Error
     * Thrable
     *
     * 定义各种异常发生的概率
     *
     * @param ctx
     * @return
     */
    @Override
    public Set<String> route(FlowContext<?> ctx) {
        return null;
    }

    @Override
    public String failover(String preNode, FlowContext<?> ctx) {
        return null;
    }

    @Override
    public Set<String> toNodes() {
        return Collections.emptySet();
    }


}
