package cn.hz.ddbm.pc.factory.buider;

import lombok.Data;

@Data
public class OrderDO {
    private   Object desc;
    private   long orderId;
    private   String orderNo;
    private  OrderStatusEnum OrderStatusEnum;


}
