package io.ddbm.pc;

import io.ddbm.pc.exception.RouterException;
import io.ddbm.pc.router.ExpressionRouter;
import io.ddbm.pc.router.NodeRouter;
import io.ddbm.pc.support.FlowContext;
import org.springframework.util.Assert;


public interface Router {

    String route(FlowContext ctx)
        throws RouterException;

    String name();

    /**
     * 从DSL构建Router
     * 1：node：到节点状态
     * 3：router：路由器
     *
     * @return
     */
    static NodeRouter buildTo(String routerName) {
        Assert.notNull(routerName, "routerName is null");
        return new NodeRouter(routerName);

    }

    static ExpressionRouter buildRouter(String routerName) {
        return new ExpressionRouter(routerName);
    }
}
