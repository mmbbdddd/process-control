package cn.hz.ddbm.pc.plugin;

import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.coast.Coasts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorPlugin implements Plugin {
    @Override
    public String code() {
        return Coasts.PLUGIN_ERROR_LOG;
    }

    @Override
    public void preAction(String name, FlowContext<?> ctx) {

    }

    @Override
    public void postAction(String name, FlowContext<?> ctx) {

    }

    @Override
    public void onActionException(String actionName, String preNode, Exception e, FlowContext<?> ctx) {
        log.error("Action错误{}:", actionName, e);
    }

    @Override
    public void onActionFinally(String name, FlowContext<?> ctx) {

    }

    @Override
    public void postRoute(String routerName, String preNode, FlowContext<?> ctx) {

    }

    @Override
    public void onRouteExcetion(String routerName, Exception e, FlowContext<?> ctx) {
        log.error("路由错误{}:", routerName, e);
    }
}
