package cn.hz.ddbm.pc.test.support;


import cn.hutool.core.math.MathUtil;

import org.junit.Test;
import org.mvel2.MVEL;

import java.math.MathContext;
import java.util.HashMap;

public class ExpressionEngineMockTest {

    @Test
    public void ognl() {
        System.out.println(MVEL.eval("Math.random()"));
        System.out.println(MVEL.eval("Math.random()>0.7"));
//        try {
//            Ognl.add
//            System.out.println(Ognl.getValue("@java.lang.Math@random() > 0.7", new HashMap(){{
//                put("match",Math.abs());
//            }}));
//        } catch (OgnlException e) {
//            e.printStackTrace();
//        }
    }
}