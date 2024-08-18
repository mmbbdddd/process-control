package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.core.Node;
import cn.hz.ddbm.pc.factory.dsl.FSM;

public enum IDCardState   {
    init("初始化"),
    sended("已发送"),
    send_failover("发送错误"),
    miss_data("客户资料缺乏"),
    miss_data_fulled("客户资料已补"),
    su("成功"),
    fail("失败"),
    error("异常");

    private final String    descr;

    IDCardState(String descr) {
        this.descr = descr; 
    }

}