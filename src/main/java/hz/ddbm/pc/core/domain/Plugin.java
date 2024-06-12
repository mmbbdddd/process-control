package hz.ddbm.pc.core.domain;

/**
 * 插件:
 * ___1,实现监控、统计等功能和业务功能分离。
 * ___2,如果业务和插件之间需要信息交互，通过上下文完成。
 */

public interface Plugin {

    void preAction(String name, BizContext ctx);

    void postAction(String name, BizContext ctx);

    void onActionException(String name, String preNode, Exception e, BizContext ctx);

    void onActionFinally(String name, BizContext ctx);

    void postRoute(String beanName, String preNode, BizContext ctx);

    void onRouteExcetion(String beanName, Exception e, BizContext ctx);
}
