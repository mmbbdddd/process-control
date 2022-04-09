package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import io.ddbm.pc.RouterException;
import org.junit.Before;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

public class FlowFactoryTest {
    GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
    Flow                         flow;
    FlowFactory                  ff;

    @Before
    public void setup() throws Exception {
        ctx.load(new ClassPathResource("s.xml"));
        ctx.refresh();
        ff   = ctx.getBean(FlowFactory.class);

    }

    @org.junit.Test
    public void get() throws RouterException {
        flow = ff.get("simple");
        Map<String, Object> args = new HashMap<>();
        args.put("name", "wanglin");
//        flow.execute((String) null, null, args);
    }

    @org.junit.Test
    public void test() throws RouterException {
        Map<String, Object> args = new HashMap<>();
        args.put("name", "wanglin");
//        flow.execute((String) null, null, args);
    }


}