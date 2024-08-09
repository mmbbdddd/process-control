package cn.hz.ddbm.pc.factory.buider;

import lombok.Data;

@Data
public class Order {
    private final Object desc;
    private final int i;

    public Order(String desc, int i) {
        this.desc = desc;
        this.i = i;
    }
}
