package cn.hz.ddbm.pc.support;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.log.Logs;
import org.springframework.stereotype.Component;

@Component("digestLogPlugin")
public class DigestLogPluginMock implements Plugin {
    @Override
    public String code() {
        return "digest";
    }

    @Override
    public void preAction(String name, FlowContext ctx) {

    }

    @Override
    public void postAction(String name, FlowContext ctx) {
    }

    @Override
    public void onActionException(String actionName, String preNode, Exception e, FlowContext ctx) {
    }

    @Override
    public void onActionFinally(String name, FlowContext ctx) {

    }

    @Override
    public void postRoute(String routerName, String preNode, FlowContext ctx) {
        Logs.digest.info("{},{},{},{}", ctx.getFlow()
                                           .getName(), ctx.getId(), preNode, ctx.getStatus()
                                                                                .getNode());
    }

    @Override
    public void onRouteExcetion(String routerName, Exception e, FlowContext ctx) {

    }
}
