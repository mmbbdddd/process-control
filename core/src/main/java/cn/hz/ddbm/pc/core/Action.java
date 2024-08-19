package cn.hz.ddbm.pc.core;


import java.util.Set;

public interface Action<S extends Enum<S>> {

    String beanName();

    void execute(FlowContext<S, ?> ctx) throws Exception;


}
