package cn.hz.ddbm.pc.core;


import lombok.Getter;

/**
 * 作用 ：节点路由
 * 类型 ：1对1（to），1对多选1(any)，1对多选多（Fork），多对1（Join）
 * dsl：
 * ___none://none
 * ___to://flowstatus
 * ___any://routerName
 * ___fork://routerName
 * ___join://conditionExpression:flowstatus
 */
public abstract class Router {
    @Getter
    String name;

    public Router(String name) {
        this.name = name;
    }

    public abstract String route(FlowContext<?> ctx);


    public abstract String failover();
}
