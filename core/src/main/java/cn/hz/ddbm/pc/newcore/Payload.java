package cn.hz.ddbm.pc.newcore;

import java.io.Serializable;

public interface Payload<S extends State> {
    String getId();

    S getState();

    void setState(S state);
}
