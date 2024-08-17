package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.core.Node;
import cn.hz.ddbm.pc.factory.dsl.StateMachineConfig;

public   enum PcState implements StateMachineConfig.State {
    init("初始化", Node.Type.START),
    sended("已发送",Node.Type.TASK),
    send_failover("发送错误",Node.Type.TASK),
    miss_data("客户资料缺乏",Node.Type.TASK),
    miss_data_fulled("客户资料已补",Node.Type.TASK),
    su("成功",Node.Type.END),
    fail("失败",Node.Type.END),
    error("异常",Node.Type.END);

    private final String    descr;
    private final Node.Type type;

    PcState(String descr, Node.Type type) {
        this.descr = descr;
        this.type  = type;
    }

    @Override
    public Node.Type type() {
        return type;
    }
}