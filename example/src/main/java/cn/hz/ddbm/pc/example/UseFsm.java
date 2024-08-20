package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.factory.dsl.FSM;

public @interface UseFsm {
    Class<?  extends FSM> value();
}
