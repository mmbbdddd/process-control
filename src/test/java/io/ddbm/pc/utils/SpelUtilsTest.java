package io.ddbm.pc.utils;

import io.ddbm.pc.RouterException;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SpelUtilsTest {

    @Test
    public void eval() throws RouterException {
        String             expression = "#result[code]=='0000'";
        Map<String,Object> args       = new HashMap<>();
        Map<String,Object> result       = new HashMap<>();
        result.put("code","0001");
        args.put("result",result);
        Assert.isTrue(!(Boolean) SpelUtils.eval(expression,args),"");
    }

}