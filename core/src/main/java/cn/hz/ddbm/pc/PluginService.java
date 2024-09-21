package cn.hz.ddbm.pc;

import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.State;
import cn.hz.ddbm.pc.newcore.log.Logs;

import java.util.HashSet;
import java.util.Set;

public class PluginService {

    public PluginService() {
    }

    public void pre(FlowContext ctx) {
        Set<Plugin> plugins = new HashSet<>(ctx.getFlow().flowAttrs().getPlugins());
        plugins.forEach((plugin) -> {
            try {
                plugin.preAction(ctx);
            } catch (Exception e) {
                Logs.error.error("{},{}", ctx.getFlow().name(), ctx.getId(), e);
            }
        });
    }

    public void post(State lastNode, FlowContext ctx) {
        Set<Plugin> plugins = new HashSet<>(ctx.getFlow().flowAttrs().getPlugins());
        plugins.forEach((plugin) -> {
            try {
                plugin.postAction(lastNode, ctx);
            } catch (Exception e) {
                Logs.error.error("{},{}", ctx.getFlow().name(), ctx.getId(), e);
            }
        });
    }

    public void error(State preNode, Exception e, FlowContext ctx) {
        Set<Plugin> plugins = new HashSet<>(ctx.getFlow().flowAttrs().getPlugins());
        plugins.forEach((plugin) -> {
            try {
                plugin.errorAction(preNode, e, ctx);
            } catch (Exception e2) {
                Logs.error.error("{},{}", ctx.getFlow().name(), ctx.getId(), e2);
            }
        });
    }

    public void _finally(State preNode, FlowContext ctx) {
        Set<Plugin> plugins = new HashSet<>(ctx.getFlow().flowAttrs().getPlugins());
        plugins.forEach((plugin) -> {
            try {
                plugin.finallyAction(preNode, ctx);
            } catch (Exception e) {
                Logs.error.error("{},{}", ctx.getFlow().name(), ctx.getId(), e);
            }
        });
    }

}
