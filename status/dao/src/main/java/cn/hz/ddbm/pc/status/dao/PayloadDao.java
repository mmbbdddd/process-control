package cn.hz.ddbm.pc.status.dao;


import cn.hz.ddbm.pc.newcore.Payload;

public interface PayloadDao {
    String flowName();

    void save(Payload data);

    Payload get(String flow);
}
