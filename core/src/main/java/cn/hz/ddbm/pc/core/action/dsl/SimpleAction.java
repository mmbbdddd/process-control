package cn.hz.ddbm.pc.core.action.dsl;

import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.utils.InfraUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface SimpleAction<S extends Enum<S>> {
    String beanName();

    void execute(FlowContext<S, ?> ctx) throws Exception;


    /**
     * 将各种action配置语法转换为特定的Action实现
     * <p>
     * 1，单action配置                     ：xxxAction
     * 2，多action配置，逗号分隔             ：xxxAction，yyyAction，zzzAction
     * 3，sagaAction配置                   ：xxxAction,（failover）  or  xxxAction,yyyyAction,zzzAction,(failover)
     * 4，noneAction配置                   ：“”   or “none”
     *
     * @param actionDsl
     * @return
     */
    String single_regexp = "\\w{1,20}";
    String multi_regexp  = "(\\w+,)+\\w+";
    String saga_regexp   = "(\\w+,)_\\w+";


    public static <S extends Enum<S>> SimpleAction<S> of(String actionDsl, FlowContext<S, ?> ctx) {
        if (null != ctx && ctx.getMockBean()) {
            if (StrUtil.isBlank(actionDsl)) {
                return null;
            } else {
                return InfraUtils.getBean("chaosAction", SimpleAction.class);
            }
        }
        if (StrUtil.isBlank(actionDsl)) {
            return null;
        }
        if (actionDsl.matches(single_regexp)) {
            return InfraUtils.getBean(actionDsl, SimpleAction.class);
        }
        if (actionDsl.matches(multi_regexp)) {
            String[] actionBeanNames = actionDsl.split(",");
            List<SimpleAction> actions = Arrays.stream(actionBeanNames)
                                         .map(name -> InfraUtils.getBean(name, SimpleAction.class))
                                         .collect(Collectors.toList());
            return new MultiAction(actionDsl, actions);
        }
        return null;
    }

}
