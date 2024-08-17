package cn.hz.ddbm.pc.example

import cn.hutool.core.util.EnumUtil
import cn.hutool.core.util.TypeUtil
import cn.hz.ddbm.pc.factory.dsl.StateMachineConfig
import org.junit.Test

import java.lang.reflect.ParameterizedType

class StateMachineConfigTest   {
    StateMachineConfig stateMachineConfig = null

    @Test
    public void   getGenerics() {
        ParameterizedType[] type =  TypeUtil.getGenerics(PcConfig.class)
        Class t2 = type[0].getActualTypeArguments()[0];
        println(EnumUtil.getEnumMap(t2).values().forEach {it.name()})
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme