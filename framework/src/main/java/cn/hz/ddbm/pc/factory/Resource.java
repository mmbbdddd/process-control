package cn.hz.ddbm.pc.factory;

import cn.hz.ddbm.pc.core.Flow;

public abstract class Resource {
    public abstract Flow resolve();

    enum Type {
        dsl,
        xml,
        json
    }
}
