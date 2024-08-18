package cn.hz.ddbm.pc.core;


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
public interface Router<S extends Enum<S>> {
    String routerName();

    /**
     * @param ctx
     * @return String : event
     */
    S route(FlowContext<S, ?> ctx);


}
