package cn.hz.ddbm.pc.factory.buider;

public enum OrderStatusEnum {
    /**待提交*/
    DRAFT,
    /**待出库*/
    SUBMITTED,
    /**已出库*/
    DELIVERING,
    /**已签收*/
    SIGNED,
    /**已完成*/
    FINISHED,
    ;
}
