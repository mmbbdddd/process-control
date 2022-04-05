package io.ddbm.pc;

import java.io.Serializable;

public interface FLowRecord {
    String translateStatusToNode();

    Serializable getId();

}
