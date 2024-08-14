package cn.hz.ddbm.pc.plugin;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.coast.Coasts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DigestLogPlugin implements Plugin {
    @Override
    public String code() {
        return Coasts.PLUGIN_DIGEST_LOG;
    }

    @Override
    public void preAction(String name, FlowContext ctx) {

    }

    @Override
    public void postAction(String name, FlowContext ctx) {
    }

    @Override
    public void onActionException(String name, String preNode, Exception e, FlowContext ctx) {
    }

    @Override
    public void onActionFinally(String name, FlowContext ctx) {

    }

    @Override
    public void postRoute(String routerName, String preNode, FlowContext ctx) {
        log.info("{},{},{},{}", ctx.getFlow()
                .getName(), ctx.getId(), preNode, ctx.getStatus()
                .getNode());
    }

    @Override
    public void onRouteExcetion(String routerName, Exception e, FlowContext ctx) {

    }
}
