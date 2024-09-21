package cn.hz.ddbm.pc.newcore.infra.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Statistics {
    AtomicInteger executeTimes;
    public Statistics(){
        this.executeTimes   = new AtomicInteger(0);
    }
}
