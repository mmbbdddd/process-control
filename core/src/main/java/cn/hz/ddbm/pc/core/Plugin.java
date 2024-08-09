package cn.hz.ddbm.pc.core;

/**
 * 插件:
 * ___1,实现监控、统计等功能和业务功能分离。
 * ___2,如果业务和插件之间需要信息交互，通过上下文完成。
 */

public interface Plugin {
    String code();

    void preAction(String name, FlowContext ctx);

    void postAction(String name, FlowContext ctx);

    void onActionException(String name, String preNode, Exception e, FlowContext ctx);

    void onActionFinally(String name, FlowContext ctx);

    void postRoute(String beanName, String preNode, FlowContext ctx);

    void onRouteExcetion(String beanName, Exception e, FlowContext ctx);
}
