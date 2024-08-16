package cn.hz.ddbm.pc.core;


public interface Action {
    String beanName();

    void execute(FlowContext<?> ctx) throws Exception;

    /**
     * 将各种action配置语法转换为特定的Action实现
     * <p>
     * 1，单action配置                     ：xxxAction
     * 2，多action配置，逗号分隔             ：xxxAction，yyyAction，zzzAction
     * 3，sagaAction配置                   ：xxxAction（failover）
     * 4，noneAction配置                   ：“”   or “none”
     *
     * @param actionDsl
     * @return
     */
    public static Action of(String actionDsl) {
        return null;
    }

}
