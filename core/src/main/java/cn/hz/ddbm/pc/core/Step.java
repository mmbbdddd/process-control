package cn.hz.ddbm.pc.core;

public interface Step {
    String status();

    interface Instant extends Step {

    }

    interface Persist extends Step {

    }
}
