package cn.hz.ddbm.pc.core;


import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.action.dsl.MultiAction;
import cn.hz.ddbm.pc.core.utils.InfraUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface Action<S extends Enum<S>> {

    String beanName();

    void execute(FlowContext<S, ?> ctx) throws Exception;

    Set<S> maybeResult();



}
