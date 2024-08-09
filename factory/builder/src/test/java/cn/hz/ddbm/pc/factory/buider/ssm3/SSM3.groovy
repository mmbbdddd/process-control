package cn.hz.ddbm.pc.factory.buider.ssm3

class SSM3 {
    public enum OrderStates {
        UNPAID,                 // 待支付
        WAITING_FOR_DELIVER,    // 待发货
        WAITING_FOR_RECEIVE,    // 待收货
        DONE                    // 结束
    }

    public enum OrderEvents {
        PAY,        // 支付
        DELIVER,    // 发货
        RECEIVE     // 收货
    }



}
