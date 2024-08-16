package cn.hz.ddbm.pc.core;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.action.MultiAction;
import cn.hz.ddbm.pc.core.action.NoneAction;
import cn.hz.ddbm.pc.core.action.SagaAction;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Action {
    String beanName();

    void execute(FlowContext<?> ctx) throws Exception;

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
    String multi_regexp  = "";
    String saga_regexp   = "";

    public static Action of(String actionDsl) {
        if (StrUtil.isBlank(actionDsl)) {
            return Coasts.NONE_ACTION;
        }
        if (actionDsl.matches(single_regexp)) {
            return InfraUtils.getBean(actionDsl, Action.class);
        }
        if (actionDsl.matches(multi_regexp)) {
            String[] actionBeanNames = actionDsl.split(",");
            List<Action> actions = Arrays.stream(actionBeanNames)
                    .map(name -> InfraUtils.getBean(name, Action.class))
                    .collect(Collectors.toList());
            return new MultiAction(actionDsl, actions);
        }
        if (actionDsl.matches(saga_regexp)) {
            String failover     = null;
            String otherPartDsl = null;
            return new SagaAction(failover, of(otherPartDsl));
        }

        return null;
    }

}
