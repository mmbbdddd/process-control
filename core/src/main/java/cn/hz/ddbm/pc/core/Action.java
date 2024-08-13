package cn.hz.ddbm.pc.core;


public interface Action {


    String beanName();

    void execute(FlowContext<?> ctx) throws Exception;

}
