package io.ddbm.pc.utils;

import io.ddbm.pc.RouterException;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class SpelUtilsTest {

    @Test
    public void eval() throws RouterException {
        String              expression = "#result[code]=='0000'";
        Map<String, Object> args       = new HashMap<>();
        Map<String, Object> result     = new HashMap<>();
        result.put("code", "0000");
        args.put("result", result);
        Assert.isTrue(SpelUtils.eval(expression, args), "");
    }

    @Test
    public void evale() throws RouterException {
        String              expression = "#result[stat]=='0000'";
        Map<String, Object> args       = new HashMap<>();
        Map<String, Object> result     = new HashMap<>();
        result.put("code", "0000");
        args.put("result", result);
        try {
            SpelUtils.eval(expression, args);
        } catch (Exception e) {
            Assert.isTrue(e instanceof RouterException);
        }
    }

}