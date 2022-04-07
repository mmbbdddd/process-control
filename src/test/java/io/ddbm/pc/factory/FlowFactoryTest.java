package io.ddbm.pc.factory;

import io.ddbm.pc.BeanConfig;
import io.ddbm.pc.Flow;
import io.ddbm.pc.RouterException;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FlowFactoryTest {
    GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
    Flow flow;

    @Before
    public void setup() throws Exception {
        ctx.load(new ClassPathResource("s.xml"));
        ctx.refresh();
        FlowFactory ff = new FlowFactory();
        flow = ff.flows.get("simple");
    }

    @org.junit.Test
    public void test() throws RouterException {
        Map<String, Object> args = new HashMap<>();
        args.put("name", "wanglin");
        flow.execute((String) null, null, args);
    }



}