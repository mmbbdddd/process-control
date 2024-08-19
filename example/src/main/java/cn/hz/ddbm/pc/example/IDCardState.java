package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.core.Node;
import cn.hz.ddbm.pc.factory.dsl.FSM;

public enum IDCardState {
    init("初始化"),
    payed("已扣款（local）"),
    sended("已发送（remote）"),
    payed_failover("扣款容错中"),
    sended_failover("发送容错中"),
    su("成功"),
    fail("失败"),
    error("异常");

    private final String descr;

    IDCardState(String descr) {
        this.descr = descr;
    }

}