package cn.hz.ddbm.pc.newcore.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.expression.ExpressionParser;

import java.util.HashMap;

import static org.mockito.Mockito.*;

class ExpressionEngineUtilsTest {
    @Mock
    ExpressionParser      parser;
    @InjectMocks
    ExpressionEngineUtils expressionEngineUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEval() { 
    }

    @Test
    void testEval2() {
    }

    @Test
    void testEval3() {
//        T result = ExpressionEngineUtils.eval("expression", new HashMap<String, Object>() {{
//            put("String", "context");
//        }}, null);
//        Assertions.assertEquals(new T(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme