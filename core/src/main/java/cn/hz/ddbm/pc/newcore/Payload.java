package cn.hz.ddbm.pc.newcore;

public interface Payload<S extends State> {
    String getId();

    S getState();

    void setState(S state);
}
