package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.core.Node;
import cn.hz.ddbm.pc.factory.dsl.FSM;

public enum IDCardState {
    init("初始化"),
    send_failover("已发送"),
    sended_ing("已发送&处理中"),
    none_sended("未返送成功"),
    miss_data("客户资料缺乏"),
    miss_data_fulled("客户资料已补"),
    wait_send("客户资料已补重发"),
    su("成功"),
    fail("失败"),
    error("异常");

    private final String descr;

    IDCardState(String descr) {
        this.descr = descr;
    }

}